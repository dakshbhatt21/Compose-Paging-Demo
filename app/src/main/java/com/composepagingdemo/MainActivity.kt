package com.composepagingdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.composepagingdemo.ui.theme.ComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTheme {
                MyAppNavHost()
            }
        }
    }

    @Composable
    fun MyAppNavHost(
        navController: NavHostController = rememberNavController(),
        startDestination: String = "home"
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("home") {
                Home(
                    onNavigateToPagingWithNetwork = {
                        navController.navigate("paging_with_network")
                    },
                    onNavigateToPagingWithNetworkAndLocal = {
                        navController.navigate("paging_with_network_and_local")
                    }
                )
            }
            composable("paging_with_network") {
                val lazyPosts: LazyPagingItems<Post> =
                    mainViewModel.posts.collectAsLazyPagingItems()
                Listing(
                    lazyPosts,
                    onNavigateToDetails = { post ->
                        mainViewModel.post = post
                        navController.navigate("details")
                    }
                )
            }
            composable("paging_with_network_and_local") {
                val lazyPosts: LazyPagingItems<Post> =
                    mainViewModel.postsSync.collectAsLazyPagingItems()
                Listing(
                    lazyPosts,
                    onNavigateToDetails = { post ->
                        mainViewModel.post = post
                        navController.navigate("details")
                    }
                )
            }
            composable(route = "details") {
                val post = mainViewModel.post
                if (post != null) {
                    Details(post = post)
                }
            }
        }
    }

    @Composable
    fun Home(
        onNavigateToPagingWithNetwork: () -> Unit,
        onNavigateToPagingWithNetworkAndLocal: () -> Unit
    ) {
        Column {
            InfoCard(
                text = "Paging Demo from remote network api",
                actionText = "Launch"
            ) {
                onNavigateToPagingWithNetwork()
            }
            InfoCard(
                text = "Paging Demo for remote network api and room database(caching)",
                actionText = "Launch"
            ) {
                onNavigateToPagingWithNetworkAndLocal()
            }
        }
    }

    @Composable
    fun InfoCard(text: String, actionText: String, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text)
                Button(onClick = { onClick() }) {
                    Text(text = actionText)
                }
            }
        }
    }
}
