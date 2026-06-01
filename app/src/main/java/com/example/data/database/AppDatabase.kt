package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DbUser::class,
        DbPost::class,
        DbComment::class,
        DbPortfolioProject::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun portfolioProjectDao(): PortfolioProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tikvik_database"
                )
                .addCallback(DatabasePrepopulateCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabasePrepopulateCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            val userDao = db.userDao()
            val postDao = db.postDao()
            val commentDao = db.commentDao()
            val portfolioDao = db.portfolioProjectDao()

            // 1. ADD DETAILED USERS
            val aria = DbUser(
                id = "aria_chen",
                username = "aria_chen",
                fullName = "Aria Chen",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
                professionalTitle = "Lead UX Designer",
                bio = "Crafting tactile mobile interactions. Coffee & brutalist UI enthusiast. 🛠️ Figma, Blender, Swift. Formerly Designer @ Airbnb.",
                followers = 12500,
                following = 482
            )
            val leo = DbUser(
                id = "leo_vance",
                username = "leo_vance",
                fullName = "Leo Vance",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                professionalTitle = "Architectural Photographer",
                bio = "Capturing geometric interactions of concrete, light, and shadows. Featured in ArchDaily. Tokyo // Berlin.",
                followers = 8900,
                following = 310
            )
            val niko = DbUser(
                id = "niko_dev",
                username = "niko_dev",
                fullName = "Niko Tesla",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
                professionalTitle = "Creative Android Developer",
                bio = "Jetpack Compose explorer and graphics shader nerd. Building the future of beautiful mobile architectures. 🤖 Kotlin, KSP, Canvas.",
                followers = 15400,
                following = 620
            )

            userDao.insertUser(aria)
            userDao.insertUser(leo)
            userDao.insertUser(niko)

            // 2. ADD INSTAGRAM-LIKE SOCIAL FEED POSTS
            val post1Id = postDao.insertPost(DbPost(
                userId = "aria_chen",
                username = aria.username,
                userAvatar = aria.avatarUrl,
                imageUri = "https://images.unsplash.com/photo-1541462608141-67571a670297?w=600",
                caption = "Nothing beats starting the day with high-fidelity canvas adjustments and premium roasted flat white. What tools do you use for prototype animation? ☕💻 #designflow #uidesign #figma",
                likesCount = 342,
                isLikedByMe = true,
                commentCount = 2
            )).toInt()

            val post2Id = postDao.insertPost(DbPost(
                userId = "leo_vance",
                username = leo.username,
                userAvatar = leo.avatarUrl,
                imageUri = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=600",
                caption = "Interlocking cement forms under the Berlin noon sun. The way shadows trace geometry is endlessly fascinating. Shot with Leica SL2. #minimalism #architecture #photography",
                likesCount = 689,
                isLikedByMe = false,
                commentCount = 1
            )).toInt()

            val post3Id = postDao.insertPost(DbPost(
                userId = "niko_dev",
                username = niko.username,
                userAvatar = niko.avatarUrl,
                imageUri = "https://images.unsplash.com/photo-1607799279861-4dd421887fb3?w=600",
                caption = "Finally got the custom multi-layered Canvas radial animation working at 120 FPS in Jetpack Compose! Absolute fluid beauty. Snippet coming to my portfolio shortly. 🚀📱 #jetpackcompose #androiddev #kotlin",
                likesCount = 1205,
                isLikedByMe = false,
                commentCount = 3
            )).toInt()

            // 3. ADD INSTAGRAM-LIKE COMMENTS
            commentDao.insertComment(DbComment(
                postId = post1Id,
                username = niko.username,
                userAvatar = niko.avatarUrl,
                commentText = "Figma handles transitions well, but interactive custom-drawn Compose paths feel so much more tactile!"
            ))
            commentDao.insertComment(DbComment(
                postId = post1Id,
                username = leo.username,
                userAvatar = leo.avatarUrl,
                commentText = "Love the natural lighting on the wooden desk layout!"
            ))

            commentDao.insertComment(DbComment(
                postId = post2Id,
                username = aria.username,
                userAvatar = aria.avatarUrl,
                commentText = "The alignment of the concrete borders is flawless. Masterful crop, Leo."
            ))

            commentDao.insertComment(DbComment(
                postId = post3Id,
                username = aria.username,
                userAvatar = aria.avatarUrl,
                commentText = "Those spring transitions look incredible. Please share the easing curve details!"
            ))
            commentDao.insertComment(DbComment(
                postId = post3Id,
                username = leo.username,
                userAvatar = leo.avatarUrl,
                commentText = "Looks super crisp."
            ))
            commentDao.insertComment(DbComment(
                postId = post3Id,
                username = "curious_coder",
                userAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
                commentText = "Is this using Modifier.graphicsLayer for acceleration? Unreal performance!"
            ))

            // 4. ADD PORTFOLIO PROJECTS (DEDICATED PROFESSIONAL PORTFOLIO SPACE)
            portfolioDao.insertProject(DbPortfolioProject(
                userId = "aria_chen",
                username = aria.username,
                userAvatar = aria.avatarUrl,
                title = "Nova Pay: Next-Gen Neobank Mobile Wallet",
                role = "Lead Mobile Designer & Brand Architect",
                description = "Nova Pay is a comprehensive mobile fintech prototype conceptualized to democratize micro-investment for tech workers. The design emphasizes geometric card elevation, gesture-based cash transfers, and a revolutionary contextual dynamic action bar. Conducted robust user research with over 150 test pilots and produced 40+ high-fidelity prototypes in Figma and After Effects.",
                coverUrl = "https://images.unsplash.com/photo-1563013544-824ae1d704d3?w=600",
                tools = "Figma, After Effects, Spline 3D, protopie",
                category = "Design",
                liveUrl = "https://figma.com/file/nova-pay-neobank",
                lovesCount = 148,
                isLovedByMe = true
            ))

            portfolioDao.insertProject(DbPortfolioProject(
                userId = "aria_chen",
                username = aria.username,
                userAvatar = aria.avatarUrl,
                title = "Komorebi: Zen Focus Timer & Audio Engine",
                role = "Co-Founder & Lead Designer",
                description = "Designed and co-developed Komorebi, an ambient white-noise focus timer themed with elegant Japanese brutalist aesthetics. Features realistic analog dials, physical knob rotation, soundscapes recorded live in Shizuoka forests, and localized mental calm sessions.",
                coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=600",
                tools = "Figma, Adobe Audition, Webflow",
                category = "Design",
                liveUrl = "https://komorebi-calm.io",
                lovesCount = 92
            ))

            portfolioDao.insertProject(DbPortfolioProject(
                userId = "leo_vance",
                username = leo.username,
                userAvatar = leo.avatarUrl,
                title = "Subterranean Light: Brutalist Metro Architecture",
                role = "Principal Photographer",
                description = "A visual exploration series focusing on the underground transit systems of Munich, Stockholm, and Tokyo. Analyzing how light bounces off cold brutalist concrete grids, turning massive public architectures into silent, geometric cathedrals. Exhibited in the Berlin Gallery of Contemporary Arts.",
                coverUrl = "https://images.unsplash.com/photo-1473163928189-364b2c4e1135?w=600",
                tools = "Leica SL2, Capture One, Adobe Lightroom",
                category = "Photography",
                liveUrl = "https://leovance.com/subterranean-light",
                lovesCount = 203
            ))

            portfolioDao.insertProject(DbPortfolioProject(
                userId = "niko_dev",
                username = niko.username,
                userAvatar = niko.avatarUrl,
                title = "AetherCompose: Advanced Shaders & UI Physics",
                role = "Solo Developer",
                description = "A open-source Jetpack Compose rendering library that implements fluid physical spring solvers, customizable fragment shaders, and advanced dynamic Canvas vector transformations entirely on the JVM. Achieves ultra-low layout latency and provides developer-friendly custom modifier extensions.",
                coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600",
                tools = "Kotlin, Jetpack Compose, AGSL, Gradle, KSP",
                category = "Development",
                liveUrl = "https://github.com/nikodev/aether-compose",
                lovesCount = 312,
                isLovedByMe = true
            ))
        }
    }
}
