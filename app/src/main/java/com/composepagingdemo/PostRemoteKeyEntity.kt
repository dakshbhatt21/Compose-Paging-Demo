package com.composepagingdemo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostRemoteKeyEntity(
    @PrimaryKey @NonNull val postId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
