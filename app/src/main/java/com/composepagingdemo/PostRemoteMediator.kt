package com.composepagingdemo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import java.io.IOException

@ExperimentalPagingApi
class PostRemoteMediator(
    private val ktorHttpClient: HttpClient,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Post>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Post>
    ): MediatorResult {
        val page = getPage(loadType, state)
            ?: return MediatorResult.Success(endOfPaginationReached = false)

        return try {
            val response = ktorHttpClient.get<List<PostRemote>>(
                "https://techcrunch.com/wp-json/wp/v2/posts?per_page=10&context=embed&page=$page"
            ).map { post ->
                Post(
                    id = post.id,
                    title = post.title.title,
                    description = post.description.description,
                    image = post.image
                )
            }

            val isEndOfList = response.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.clearAllTables()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    PostRemoteKeyEntity(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.postRemoteKeyDao.insertAll(keys)
                appDatabase.postDao.insertPosts(response)
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getPage(
        loadType: LoadType,
        state: PagingState<Int, Post>
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Post>): PostRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { postId ->
                appDatabase.postRemoteKeyDao.remoteKeysPostId(postId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Post>): PostRemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { post -> appDatabase.postRemoteKeyDao.remoteKeysPostId(post.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Post>): PostRemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { post -> appDatabase.postRemoteKeyDao.remoteKeysPostId(post.id) }
    }
}