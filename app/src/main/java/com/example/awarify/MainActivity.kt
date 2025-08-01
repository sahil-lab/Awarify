package com.example.awarify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.awarify.navigation.NavigationDestinations
import com.example.awarify.navigation.NavigationArguments
import com.example.awarify.notifications.NotificationHelper
import com.example.awarify.ui.ViewModelFactory
import com.example.awarify.ui.components.BottomNavigationBar
import com.example.awarify.ui.screens.home.HomeScreen
import com.example.awarify.ui.screens.home.HomeViewModel
import com.example.awarify.ui.screens.hobbies.HobbiesScreen
import com.example.awarify.ui.screens.hobbies.HobbiesViewModel
import com.example.awarify.ui.screens.hobbies.AddHobbyScreen
import com.example.awarify.ui.screens.hobbies.AddHobbyViewModel
import com.example.awarify.ui.screens.calendar.CalendarScreen
import com.example.awarify.ui.screens.timetable.TimetableScreen
import com.example.awarify.ui.screens.settings.SettingsScreen
import com.example.awarify.ui.theme.HobbyTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize notification channel
        NotificationHelper.createNotificationChannel(this)
        
        enableEdgeToEdge()
        setContent {
            HobbyTrackerTheme {
                HobbyTrackerApp()
            }
        }
    }
}

@Composable
fun HobbyTrackerApp() {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val application = navController.context.applicationContext as HobbyTrackerApplication
    val viewModelFactory = ViewModelFactory(
        hobbyRepository = application.hobbyRepository,
        taskRepository = application.taskRepository,
        notificationRepository = application.notificationRepository,
        hobbySessionRepository = application.hobbySessionRepository,
        todoRepository = application.todoRepository,
        context = navController.context
    )
    
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.HOME.route,
        modifier = modifier
    ) {
        composable(NavigationDestinations.HOME.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        
        composable(NavigationDestinations.HOBBIES.route) {
            val hobbiesViewModel: HobbiesViewModel = viewModel(factory = viewModelFactory)
            HobbiesScreen(navController = navController, viewModel = hobbiesViewModel)
        }
        
        composable(NavigationDestinations.ADD_HOBBY.route) {
            val addHobbyViewModel: AddHobbyViewModel = viewModel(factory = viewModelFactory)
            AddHobbyScreen(navController = navController, viewModel = addHobbyViewModel)
        }
        
        composable(
            route = NavigationDestinations.EDIT_HOBBY.route,
            arguments = listOf(navArgument(NavigationArguments.HOBBY_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val hobbyId = backStackEntry.arguments?.getLong(NavigationArguments.HOBBY_ID) ?: 0L
            val addHobbyViewModel: AddHobbyViewModel = viewModel(factory = viewModelFactory)
            AddHobbyScreen(
                navController = navController, 
                viewModel = addHobbyViewModel,
                hobbyId = hobbyId
            )
        }
        
        composable(NavigationDestinations.CALENDAR.route) {
            CalendarScreen(navController = navController)
        }
        
        composable(NavigationDestinations.TIMETABLE.route) {
            TimetableScreen(navController = navController)
        }
        
        composable(NavigationDestinations.SETTINGS.route) {
            SettingsScreen(navController = navController)
        }
    }
}