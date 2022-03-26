package com.mnowo.offlineschoolmanager.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mnowo.offlineschoolmanager.*

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination =  Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.TimetableScreen.route) {
            TimetableScreen()
        }

        composable(Screen.SubjectScreen.route) {
            SubjectScreen()
        }

        composable(Screen.GradeScreen.route) {
            GradeScreen()
        }

        composable(Screen.ToDoScreen.route) {
            ToDoScreen()
        }

        composable(Screen.ExamScreen.route) {
            ExamScreen()
        }
    }
}