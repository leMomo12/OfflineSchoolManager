package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen

@Composable
fun BottomAppBar(
    home: Boolean? = null,
    timetable: Boolean? = null,
    gradeAverage: Boolean? = null,
    toDo: Boolean? = null,
    exam: Boolean? = null,
    onClick: (Screen) -> Unit
) {
    BottomAppBar(backgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White)  {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                onClick(Screen.HomeScreen)
            }) {
                if (isSystemInDarkTheme()) Color.Gray else Color.Black
                if (home == true) {
                    Icon(Icons.Rounded.Home, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                } else {
                    Icon(Icons.Outlined.Home, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                }
            }
            IconButton(onClick = {
                onClick(Screen.TimetableScreen)
            }) {
                if (timetable == true) {
                    Icon(Icons.Rounded.AccessAlarm, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                } else {
                    Icon(Icons.Outlined.AccessAlarm, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                }
            }
            IconButton(onClick = {
                onClick(Screen.SubjectScreen)
            }) {
                if (gradeAverage == true) {
                    Icon(Icons.Rounded.School, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                } else {
                    Icon(Icons.Outlined.School, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                }
            }
            IconButton(onClick = {
                onClick(Screen.ToDoScreen)
            }) {
                if (toDo == true) {
                    Icon(Icons.Rounded.Edit, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                } else {
                    Icon(Icons.Outlined.Edit, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                }
            }
            IconButton(onClick = {
                onClick(Screen.ExamScreen)
            }) {
                if (exam == true) {
                    Icon(Icons.Rounded.Assignment, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                } else {
                    Icon(Icons.Outlined.Assignment, contentDescription = "", tint = if (isSystemInDarkTheme()) Color.Gray else Color.Black)
                }
            }
        }
    }
}