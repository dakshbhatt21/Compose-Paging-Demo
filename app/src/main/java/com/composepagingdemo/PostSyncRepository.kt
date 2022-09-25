package com.composepagingdemo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class PostSyncRepository(
    private val ktorHttpClient: HttpClient,
    private val appDatabase: AppDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPosts(): Flow<PagingData<Post>> {
        val pagingSourceFactory = {
            appDatabase.postDao.getAllPosts()
        }

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                jumpThreshold = Int.MIN_VALUE,
                enablePlaceholders = true,
            ),
            remoteMediator = PostRemoteMediator(ktorHttpClient, appDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}