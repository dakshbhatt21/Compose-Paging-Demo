package com.composepagingdemo

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class PostRepository(private val ktorHttpClient: HttpClient) {
    suspend fun getPosts(page: Int): List<Post> = ktorHttpClient.get<List<PostRemote>>(
        "https://techcrunch.com/wp-json/wp/v2/posts?per_page=10&context=embed&page=$page"
    ).map { post ->
        Post(post.title.title, post.description.description, post.image)
    }
}