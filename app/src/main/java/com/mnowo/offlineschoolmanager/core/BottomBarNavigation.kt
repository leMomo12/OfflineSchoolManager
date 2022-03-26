package com.mnowo.offlineschoolmanager.core

sealed class BottomBarNavigation {
    object NavToHomeScreen : BottomBarNavigation()
    object NavToTimetableScreen : BottomBarNavigation()
    object NavToSubjectScreen : BottomBarNavigation()
    object NavToToDoScreen : BottomBarNavigation()
    object NavToExamScreen : BottomBarNavigation()
}
