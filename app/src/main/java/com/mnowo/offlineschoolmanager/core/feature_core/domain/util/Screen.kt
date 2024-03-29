package com.mnowo.offlineschoolmanager.core.feature_core.domain.util

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object TimetableScreen : Screen("timetable_screen")
    object SubjectScreen : Screen("subject_screen")
    object GradeScreen : Screen("grade_screen")
    object ToDoScreen : Screen("todo_screen")
    object ExamScreen : Screen("exam_screen")
    object SettingsScreen : Screen("settings_screen")
}
