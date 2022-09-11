package com.composepagingdemo

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postSource: PostSource
) : ViewModel() {

    val posts: Flow<PagingData<Post>> = Pager(PagingConfig(pageSize = 10)) { postSource }.flow

    var post: Post? = null
}