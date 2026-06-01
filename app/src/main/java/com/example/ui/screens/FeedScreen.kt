package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.database.DbPost
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: MainViewModel,
    onNavigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.allPosts.collectAsStateWithLifecycle()
    val users by viewModel.allUsers.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    
    val activePostIdForComments by viewModel.activePostIdForComments.collectAsStateWithLifecycle()
    val comments by viewModel.activeComments.collectAsStateWithLifecycle()
    
    var showCommentDialog by remember { mutableStateOf(false) }

    // Toggle dialog when post id for comments is updated
    LaunchedEffect(activePostIdForComments) {
        showCommentDialog = activePostIdForComments != null
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Toolbar
            TopAppBar(
                title = {
                    Text(
                        text = "TikVik",
                        style = androidx.compose.ui.text.TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF6750A4), Color(0xFFB3261E))
                            ),
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Activity Alerts", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Direct Messages", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.testTag("feed_toolbar")
            )

            // Feed Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Stories Bar / Professionals Online
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "Featured Professionals Online",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                        
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Current user story if logged in
                            currentUser?.let { user ->
                                item {
                                    StoryItem(
                                        username = "You",
                                        avatarUrl = user.avatarUrl,
                                        role = "Your Dashboard",
                                        onClick = { onNavigateToProfile(user.id) }
                                    )
                                }
                            }

                            // Other professional stories
                            items(users.filter { it.id != currentUser?.id }) { user ->
                                StoryItem(
                                    username = user.username,
                                    avatarUrl = user.avatarUrl,
                                    role = user.professionalTitle,
                                    onClick = { onNavigateToProfile(user.id) }
                                )
                            }
                        }
                        
                        Divider(
                            modifier = Modifier.padding(top = 12.dp),
                            color = Color(0xFF222222),
                            thickness = 0.5.dp
                        )
                    }
                }

                // Empty feed fallback
                if (posts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Outlined.Image, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No social posts yet", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Be the first to share an update on TikVik!", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }

                // Feed Posts list
                items(posts) { post ->
                    PostCard(
                        post = post,
                        onLikeClick = { viewModel.toggleLikePost(post.id) },
                        onCommentClick = { 
                            viewModel.setPostForComments(post.id)
                        },
                        onUserClick = { onNavigateToProfile(post.userId) }
                    )
                }
            }
        }

        // Comment Sheet Popup Dialog
        if (showCommentDialog && activePostIdForComments != null) {
            Dialog(
                onDismissRequest = { 
                    viewModel.setPostForComments(null)
                    showCommentDialog = false
                }
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(vertical = 16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Comments (${comments.size})",
                                style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                            IconButton(onClick = { 
                                viewModel.setPostForComments(null)
                                showCommentDialog = false
                            }) {
                                Icon(Icons.Outlined.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }

                        Divider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))

                        // Comments List
                        LazyColumn(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (comments.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No comments yet. Start the conversation!", color = Color.Gray, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                items(comments) { comment ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        AsyncImage(
                                            model = comment.userAvatar,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color.DarkGray),
                                            contentScale = ContentScale.Crop
                                        )
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = comment.username,
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "just now",
                                                    color = Color.Gray,
                                                    fontSize = 10.sp
                                                )
                                            }
                                            Text(
                                                text = comment.commentText,
                                                color = Color.LightGray,
                                                fontSize = 13.sp,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Comment input field
                        var newCommentText by remember { mutableStateOf("") }
                        val focusManager = LocalFocusManager.current
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .background(Color(0xFF2E2E2E), RoundedCornerShape(24.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = newCommentText,
                                onValueChange = { newCommentText = it },
                                placeholder = { Text("Add comment as ${currentUser?.username ?: "user"}...", color = Color.Gray, fontSize = 13.sp) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.weight(1f).testTag("comment_field"),
                                maxLines = 2
                            )
                            IconButton(
                                onClick = {
                                    if (newCommentText.isNotBlank() && activePostIdForComments != null) {
                                        viewModel.submitComment(activePostIdForComments!!, newCommentText)
                                        newCommentText = ""
                                        focusManager.clearFocus()
                                    }
                                },
                                modifier = Modifier.testTag("submit_comment_button")
                            ) {
                                Icon(Icons.Filled.Send, contentDescription = "Submit comment", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoryItem(
    username: String,
    avatarUrl: String,
    role: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .width(72.dp)
    ) {
        val pinkOrangePurpleGrad = Brush.linearGradient(
            colors = listOf(
                Color(0xFF6750A4),
                Color(0xFFB3261E),
                Color(0xFFFFA000)
            )
        )

        Box(
            modifier = Modifier
                .size(62.dp)
                .border(2.dp, pinkOrangePurpleGrad, CircleShape)
                .background(MaterialTheme.colorScheme.background, CircleShape)
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "$username Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = username,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = role,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.sp,
                lineHeight = 9.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PostCard(
    post: DbPost,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .testTag("post_card_${post.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onUserClick() }
                ) {
                    AsyncImage(
                        model = post.userAvatar,
                        contentDescription = "${post.username} thumbnail",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = post.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Active Professional",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.MoreVert, contentDescription = "Options", tint = MaterialTheme.colorScheme.onBackground)
                }
            }

            // Post Image with custom gradient mock-fallback if URL fails
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.primaryContainer)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = post.imageUri,
                    contentDescription = "Post Artwork",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Interaction Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Like Button
                    IconButton(
                        onClick = onLikeClick,
                        modifier = Modifier.testTag("like_button_${post.id}")
                    ) {
                        if (post.isLikedByMe) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Unlike",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(28.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Comment Button
                    IconButton(
                        onClick = onCommentClick,
                        modifier = Modifier.testTag("comment_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // Share Button
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Save (Bookmark) Button
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Feed Text Descriptions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
            ) {
                // Likes Count
                Text(
                    text = "${post.likesCount} likes",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Caption text
                Text(
                    text = post.caption,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (post.commentCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "View all ${post.commentCount} comments",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { onCommentClick() }
                            .padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
