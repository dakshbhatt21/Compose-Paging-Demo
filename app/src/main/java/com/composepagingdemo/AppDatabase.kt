package com.composepagingdemo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Post::class, PostRemoteKeyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val postDao: PostDao
    abstract val postRemoteKeyDao: PostRemoteKeyDao
}