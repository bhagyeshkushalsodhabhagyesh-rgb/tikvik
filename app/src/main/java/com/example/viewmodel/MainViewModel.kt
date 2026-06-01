package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = TikVikRepository(
        database.userDao(),
        database.postDao(),
        database.commentDao(),
        database.portfolioProjectDao()
    )

    // Current logged-in user state
    private val _currentUserId = MutableStateFlow("niko_dev")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    // Observe changes to currentUserId and get associated DbUser
    val currentUser: StateFlow<DbUser?> = _currentUserId
        .flatMapLatest { id -> repository.getUserById(id) }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Lists
    val allUsers: StateFlow<List<DbUser>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPosts: StateFlow<List<DbPost>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProjects: StateFlow<List<DbPortfolioProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Comments for active selected post
    private val _activePostIdForComments = MutableStateFlow<Int?>(null)
    val activePostIdForComments: StateFlow<Int?> = _activePostIdForComments.asStateFlow()

    val activeComments: StateFlow<List<DbComment>> = _activePostIdForComments
        .flatMapLatest { id ->
            if (id != null) repository.getCommentsForPost(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and Categories
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPortfolioCategory = MutableStateFlow("All")
    val selectedPortfolioCategory: StateFlow<String> = _selectedPortfolioCategory.asStateFlow()

    // Filtered projects
    val filteredProjects: StateFlow<List<DbPortfolioProject>> = combine(
        allProjects,
        _searchQuery,
        _selectedPortfolioCategory
    ) { projects, query, category ->
        projects.filter { project ->
            val matchesCategory = category == "All" || project.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isEmpty() || 
                    project.title.contains(query, ignoreCase = true) ||
                    project.description.contains(query, ignoreCase = true) ||
                    project.tools.contains(query, ignoreCase = true) ||
                    project.username.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions
    fun setCurrentUser(userId: String) {
        _currentUserId.value = userId
    }

    fun setPostForComments(postId: Int?) {
        _activePostIdForComments.value = postId
    }

    fun submitComment(postId: Int, text: String) {
        val user = currentUser.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(
                postId = postId,
                username = user.username,
                userAvatar = user.avatarUrl,
                text = text
            )
        }
    }

    fun toggleLikePost(postId: Int) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
        }
    }

    fun toggleLoveProject(projectId: Int) {
        viewModelScope.launch {
            repository.toggleLoveProject(projectId)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setPortfolioCategory(category: String) {
        _selectedPortfolioCategory.value = category
    }

    fun uploadPost(imageUri: String, caption: String, onSuccess: () -> Unit) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val newPost = DbPost(
                userId = user.id,
                username = user.username,
                userAvatar = user.avatarUrl,
                imageUri = imageUri.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600" },
                caption = caption,
                likesCount = 0,
                commentCount = 0
            )
            repository.createPost(newPost)
            onSuccess()
        }
    }

    fun uploadProject(
        title: String,
        role: String,
        description: String,
        coverUrl: String,
        tools: String,
        category: String,
        liveUrl: String,
        onSuccess: () -> Unit
    ) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val newProject = DbPortfolioProject(
                userId = user.id,
                username = user.username,
                userAvatar = user.avatarUrl,
                title = title,
                role = role,
                description = description,
                coverUrl = coverUrl.ifBlank { "https://images.unsplash.com/photo-1541462608141-67571a670297?w=600" },
                tools = tools,
                category = category,
                liveUrl = liveUrl,
                lovesCount = 0
            )
            repository.createProject(newProject)
            onSuccess()
        }
    }
}
