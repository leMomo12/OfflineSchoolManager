package com.mnowo.offlineschoolmanager.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mnowo.offlineschoolmanager.*
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination =  Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.TimetableScreen.route) {
            TimetableScreen(navController = navController)
        }

        composable(Screen.SubjectScreen.route) {
            SubjectScreen(navController = navController)
        }

        composable(Screen.GradeScreen.route) {
            GradeScreen()
        }

        composable(Screen.ToDoScreen.route) {
            ToDoScreen(navController = navController)
        }

        composable(Screen.ExamScreen.route) {
            ExamScreen(navController = navController)
        }
    }
}