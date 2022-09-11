package com.composepagingdemo

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PostSource(
    private val postRepository: PostRepository
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val nextPage = params.key ?: 1
            val posts = postRepository.getPosts(nextPage)

            LoadResult.Page(
                data = posts,
                prevKey = if (nextPage == 1) null else nextPage.minus(1),
                nextKey = if (posts.isEmpty()) null else nextPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition
    }
}