package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class DbUser(
    @PrimaryKey val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String,
    val professionalTitle: String,
    val bio: String,
    val followers: Int,
    val following: Int
)

@Entity(tableName = "posts")
data class DbPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val username: String,
    val userAvatar: String,
    val imageUri: String,
    val caption: String,
    val likesCount: Int,
    val isLikedByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val commentCount: Int = 0
)

@Entity(tableName = "comments")
data class DbComment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val username: String,
    val userAvatar: String,
    val commentText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "portfolio_projects")
data class DbPortfolioProject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val username: String,
    val userAvatar: String,
    val title: String,
    val role: String,
    val description: String,
    val coverUrl: String,
    val tools: String, // comma separated
    val category: String, // "Design", "Development", "Photography", "Art"
    val liveUrl: String,
    val lovesCount: Int = 0,
    val isLovedByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
