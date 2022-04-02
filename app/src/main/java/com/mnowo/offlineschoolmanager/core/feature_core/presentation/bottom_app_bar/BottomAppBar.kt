package com.mnowo.offlineschoolmanager

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
    BottomAppBar(backgroundColor = Color.White) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                onClick(Screen.HomeScreen)
            }) {
                if(home == true) {
                    Icon(Icons.Rounded.Home, contentDescription = "")
                } else {
                    Icon(Icons.Outlined.Home, contentDescription = "")
                }
            }
            IconButton(onClick = {
                onClick(Screen.TimetableScreen)
            }) {
                if(timetable == true) {
                    Icon(Icons.Rounded.AccessAlarm, contentDescription = "")
                } else {
                    Icon(Icons.Outlined.AccessAlarm, contentDescription = "")
                }
            }
            IconButton(onClick = {
                onClick(Screen.SubjectScreen)
            }) {
                if(gradeAverage == true) {
                    Icon(Icons.Rounded.Equalizer, contentDescription = "")
                } else {
                    Icon(Icons.Outlined.Equalizer, contentDescription = "")
                }
            }
            IconButton(onClick = {
                onClick(Screen.ToDoScreen)
            }) {
                if(toDo == true) {
                    Icon(Icons.Rounded.Edit, contentDescription = "")
                } else {
                    Icon(Icons.Outlined.Edit, contentDescription = "")
                }
            }
            IconButton(onClick = {
                onClick(Screen.ExamScreen)
            }) {
                if(exam == true) {
                    Icon(Icons.Rounded.PlaylistAdd, contentDescription = "")
                } else {
                    Icon(Icons.Outlined.PlaylistAdd, contentDescription = "")
                }
            }
        }
    }
}