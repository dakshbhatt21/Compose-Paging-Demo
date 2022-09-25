package com.composepagingdemo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey(autoGenerate = true) @NonNull val postId: Int = 0,
    @NonNull val id: Int,
    @NonNull val title: String,
    @NonNull val description: String,
    @NonNull val image: String
)