package com.composepagingdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postSource: PostSource
) : ViewModel() {

    val posts: Flow<PagingData<Post>> = Pager(PagingConfig(pageSize = 10)) { postSource }
        .flow   // convert to Flow
        .cachedIn(viewModelScope) // cache data in viewmodel scope to avoid frequent api calls

    var post: Post? = null
}