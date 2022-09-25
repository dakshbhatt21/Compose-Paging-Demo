package com.composepagingdemo

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {
    @Query("SELECT * FROM Post")
    fun getAllPosts(): PagingSource<Int, Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)
}