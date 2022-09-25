package com.composepagingdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PostRemoteKeyEntity>)

    @Query("SELECT * FROM PostRemoteKeyEntity WHERE postId = :id")
    suspend fun remoteKeysPostId(id: Int): PostRemoteKeyEntity?
}