package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateScreen(
    viewModel: MainViewModel,
    onNavigateToFeed: () -> Unit,
    onNavigateToPortfolio: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Social Post, 1 = Portfolio Project
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Social Post states
    var socialImgUrl by remember { mutableStateOf("") }
    var captionText by remember { mutableStateOf("") }

    // Portfolio Project states
    var projTitle by remember { mutableStateOf("") }
    var projRole by remember { mutableStateOf("") }
    var projTools by remember { mutableStateOf("") }
    var projCoverUrl by remember { mutableStateOf("") }
    var projLiveUrl by remember { mutableStateOf("") }
    var projDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Design") }
    val categoriesList = listOf("Design", "Development", "Photography", "Art")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Core Header
        item {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = "Publish Workshop",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    ),
                    modifier = Modifier.testTag("publish_title")
                )
                Text(
                    text = "Share quick updates or add premium work to your showcase space",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Custom Sliding Toggle
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Tab 0
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { selectedTab = 0 }
                        .padding(vertical = 12.dp)
                        .testTag("social_post_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.PhotoCamera, 
                            contentDescription = null, 
                            tint = if (selectedTab == 0) Color.Black else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Social Update", 
                            color = if (selectedTab == 0) Color.Black else Color.Gray, 
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Tab 1
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { selectedTab = 1 }
                        .padding(vertical = 12.dp)
                        .testTag("portfolio_project_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.BusinessCenter, 
                            contentDescription = null, 
                            tint = if (selectedTab == 1) Color.Black else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Professional Portfolio", 
                            color = if (selectedTab == 1) Color.Black else Color.Gray, 
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Action Fields based on selection
        if (selectedTab == 0) {
            // SOCIAL POST WORKSHOP
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF131313), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("CREATE A NEW INSTAGRAM-STYLE SOCIAL POST", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Image input
                    OutlinedTextField(
                        value = socialImgUrl,
                        onValueChange = { socialImgUrl = it },
                        label = { Text("Image URL link", color = Color.Gray) },
                        placeholder = { Text("https://images.unsplash.com/...", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag("social_img_url_input"),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Outlined.InsertLink, contentDescription = null, tint = Color.Gray) }
                    )

                    // Image preview card
                    if (socialImgUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(bottom = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = socialImgUrl,
                                contentDescription = "Active image preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Caption
                    OutlinedTextField(
                        value = captionText,
                        onValueChange = { captionText = it },
                        label = { Text("Caption with hashtags", color = Color.Gray) },
                        placeholder = { Text("Having an amazing creative workflow brainstorm... #inspiration #design", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(bottom = 20.dp)
                            .testTag("social_caption_input"),
                        maxLines = 4,
                        leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null, tint = Color.Gray) }
                    )

                    // User identity badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = currentUser?.avatarUrl,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Publishing as @${currentUser?.username ?: "anonymous"}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Will be shared live into the TikVik Social Feed", color = Color.Gray, fontSize = 10.sp)
                        }
                    }

                    // Submit
                    Button(
                        onClick = {
                            if (captionText.isBlank()) {
                                Toast.makeText(context, "Please write a caption!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            // Default mock fallback if no URL is specified
                            val finalUrl = socialImgUrl.ifBlank {
                                "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600"
                            }
                            viewModel.uploadPost(finalUrl, captionText) {
                                Toast.makeText(context, "Instagram post uploaded successfully!", Toast.LENGTH_SHORT).show()
                                // Clear
                                socialImgUrl = ""
                                captionText = ""
                                onNavigateToFeed()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_social_post"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Share tikvik Update", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        } else {
            // PORTFOLIO PROJECT WORKSHOP
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF131313), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("ADD COMPREHENSIVE CASE STUDY TO PORTFOLIO SPACE", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Project Title
                    OutlinedTextField(
                        value = projTitle,
                        onValueChange = { projTitle = it },
                        label = { Text("Project Title", color = Color.Gray) },
                        placeholder = { Text("e.g. Komorebi Focus App", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("portfolio_title_input"),
                        singleLine = true
                    )

                    // Professional Role in Project
                    OutlinedTextField(
                        value = projRole,
                        onValueChange = { projRole = it },
                        label = { Text("Your Role", color = Color.Gray) },
                        placeholder = { Text("e.g. Lead Mobile Designer & Developer", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("portfolio_role_input"),
                        singleLine = true
                    )

                    // Tools & Stack
                    OutlinedTextField(
                        value = projTools,
                        onValueChange = { projTools = it },
                        label = { Text("Tools Used (comma separated)", color = Color.Gray) },
                        placeholder = { Text("e.g. Figma, Blending, Kotlin, Room", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("portfolio_tools_input"),
                        singleLine = true
                    )

                    // Cover Art image Link
                    OutlinedTextField(
                        value = projCoverUrl,
                        onValueChange = { projCoverUrl = it },
                        label = { Text("Project Cover Image URL", color = Color.Gray) },
                        placeholder = { Text("https://images.unsplash.com/...", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("portfolio_cover_input"),
                        singleLine = true
                    )

                    if (projCoverUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = projCoverUrl,
                                contentDescription = "Active project preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Live Project link
                    OutlinedTextField(
                        value = projLiveUrl,
                        onValueChange = { projLiveUrl = it },
                        label = { Text("Live Prototype / Code URL link", color = Color.Gray) },
                        placeholder = { Text("https://github.com/...", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("portfolio_url_input"),
                        singleLine = true
                    )

                    // Category Selection Chips
                    Text("CHOOSE PROJECT CATEGORY", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                    ContextGridFlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        categoriesList.forEach { category ->
                            val isSelected = selectedCategory == category
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp, bottom = 6.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color(0xFF222222))
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("create_project_category_$category")
                            ) {
                                Text(category, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Detailed description
                    OutlinedTextField(
                        value = projDescription,
                        onValueChange = { projDescription = it },
                        label = { Text("Case Study Overview", color = Color.Gray) },
                        placeholder = { Text("Describe the creative process, problems solved, and solutions delivered in this project layout...", color = Color.DarkGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color(0xFF333333)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(bottom = 20.dp)
                            .testTag("portfolio_desc_input"),
                        maxLines = 8
                    )

                    // Submit Button
                    Button(
                        onClick = {
                            if (projTitle.isBlank() || projRole.isBlank() || projTools.isBlank() || projDescription.isBlank()) {
                                Toast.makeText(context, "Please fill out all required fields!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val finalCover = projCoverUrl.ifBlank {
                                "https://images.unsplash.com/photo-1541462608141-67571a670297?w=600"
                            }
                            viewModel.uploadProject(
                                title = projTitle,
                                role = projRole,
                                description = projDescription,
                                coverUrl = finalCover,
                                tools = projTools,
                                category = selectedCategory,
                                liveUrl = projLiveUrl.ifBlank { "https://tikvik.com/sandbox" }
                            ) {
                                Toast.makeText(context, "Portfolio Case Study added!", Toast.LENGTH_SHORT).show()
                                // Clear
                                projTitle = ""
                                projRole = ""
                                projTools = ""
                                projCoverUrl = ""
                                projLiveUrl = ""
                                projDescription = ""
                                onNavigateToPortfolio()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_portfolio_project"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = Color.White
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.VerifiedUser, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PUBLISH PORTFOLIO CASE STUDY", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

// Basic Flow-like wrapping container to replace Accompanist-FlowRow to keep things native and quick
@Composable
fun ContextGridFlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}
