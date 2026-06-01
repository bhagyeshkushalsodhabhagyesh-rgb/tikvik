package com.example.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class TikVikRepository(
    private val userDao: UserDao,
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val portfolioDao: PortfolioProjectDao
) {
    // Users
    val allUsers: Flow<List<DbUser>> = userDao.getAllUsers()
    
    fun getUserById(userId: String): Flow<DbUser?> {
        return userDao.getUserById(userId)
    }

    suspend fun saveUser(user: DbUser) {
        userDao.insertUser(user)
    }

    // Posts
    val allPosts: Flow<List<DbPost>> = postDao.getAllPosts()

    fun getPostsByUserId(userId: String): Flow<List<DbPost>> {
        return postDao.getPostsByUserId(userId)
    }

    fun getPostById(postId: Int): Flow<DbPost?> {
        return postDao.getPostById(postId)
    }

    suspend fun createPost(post: DbPost): Long {
        return postDao.insertPost(post)
    }

    suspend fun toggleLikePost(postId: Int) {
        val currentPost = postDao.getPostById(postId).firstOrNull() ?: return
        val newIsLiked = !currentPost.isLikedByMe
        val newLikesCount = if (newIsLiked) currentPost.likesCount + 1 else maxOf(0, currentPost.likesCount - 1)
        val updatedPost = currentPost.copy(
            isLikedByMe = newIsLiked,
            likesCount = newLikesCount
        )
        postDao.updatePost(updatedPost)
    }

    // Comments
    fun getCommentsForPost(postId: Int): Flow<List<DbComment>> {
        return commentDao.getCommentsForPost(postId)
    }

    suspend fun addComment(postId: Int, username: String, userAvatar: String, text: String): Long {
        val comment = DbComment(
            postId = postId,
            username = username,
            userAvatar = userAvatar,
            commentText = text
        )
        val commentId = commentDao.insertComment(comment)
        
        // Update post's comment count
        val post = postDao.getPostById(postId).firstOrNull()
        if (post != null) {
            postDao.updatePost(post.copy(commentCount = post.commentCount + 1))
        }
        return commentId
    }

    // Portfolio Projects
    val allProjects: Flow<List<DbPortfolioProject>> = portfolioDao.getAllProjects()

    fun getProjectsByUserId(userId: String): Flow<List<DbPortfolioProject>> {
        return portfolioDao.getProjectsByUserId(userId)
    }

    fun getProjectsByCategory(category: String): Flow<List<DbPortfolioProject>> {
        return portfolioDao.getProjectsByCategory(category)
    }

    suspend fun createProject(project: DbPortfolioProject): Long {
        return portfolioDao.insertProject(project)
    }

    suspend fun toggleLoveProject(projectId: Int) {
        val currentProject = portfolioDao.getProjectById(projectId).firstOrNull() ?: return
        val newIsLoved = !currentProject.isLovedByMe
        val newLovesCount = if (newIsLoved) currentProject.lovesCount + 1 else maxOf(0, currentProject.lovesCount - 1)
        val updatedProject = currentProject.copy(
            isLovedByMe = newIsLoved,
            lovesCount = newLovesCount
        )
        portfolioDao.updateProject(updatedProject)
    }

    suspend fun deleteProject(project: DbPortfolioProject) {
        portfolioDao.deleteProject(project)
    }
}
