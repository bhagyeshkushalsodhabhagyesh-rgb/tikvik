package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<DbUser>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: String): Flow<DbUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DbUser)
}

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<DbPost>>

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY timestamp DESC")
    fun getPostsByUserId(userId: String): Flow<List<DbPost>>

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    fun getPostById(postId: Int): Flow<DbPost?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: DbPost): Long

    @Update
    suspend fun updatePost(post: DbPost)

    @Delete
    suspend fun deletePost(post: DbPost)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Int): Flow<List<DbComment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: DbComment): Long
}

@Dao
interface PortfolioProjectDao {
    @Query("SELECT * FROM portfolio_projects ORDER BY timestamp DESC")
    fun getAllProjects(): Flow<List<DbPortfolioProject>>

    @Query("SELECT * FROM portfolio_projects WHERE userId = :userId ORDER BY timestamp DESC")
    fun getProjectsByUserId(userId: String): Flow<List<DbPortfolioProject>>

    @Query("SELECT * FROM portfolio_projects WHERE category = :category ORDER BY timestamp DESC")
    fun getProjectsByCategory(category: String): Flow<List<DbPortfolioProject>>

    @Query("SELECT * FROM portfolio_projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: Int): Flow<DbPortfolioProject?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: DbPortfolioProject): Long

    @Update
    suspend fun updateProject(project: DbPortfolioProject)

    @Delete
    suspend fun deleteProject(project: DbPortfolioProject)
}
