package com.composepagingdemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage

// region :: home
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Listing(lazyPosts: LazyPagingItems<Post>, onNavigateToDetails: (Post) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Home")
                }
            )
        }, content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colorScheme.background
            ) {
                ListItems(lazyPosts) { post -> onNavigateToDetails(post) }
            }
        })
}

@Composable
fun ListItems(posts: LazyPagingItems<Post>, onItemTapped: (Post) -> Unit) {
    val listState: LazyListState = rememberLazyListState()
    // logic to remember the index while navigation
    when (posts.itemCount) {
        0 -> LoadingItem()
        else -> {
            LazyColumn(state = listState) {
                items(items = posts) { post ->
                    Row(modifier = Modifier.clickable { if (post != null) onItemTapped(post) }) {
                        AsyncImage(
                            model = post?.image,
                            contentDescription = null,
                            modifier = Modifier.width(96.dp),
                            contentScale = ContentScale.Inside,
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = post?.title.orEmpty(),
                            fontSize = 12.sp
                        )
                    }
                    Divider(color = Color.Black)
                }

                posts.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
// endregion