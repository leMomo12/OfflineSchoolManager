package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.composesurveyapp.core.presentation.util.UiEvent
import com.mnowo.offlineschoolmanager.core.Screen
import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TimetableScreen(navController: NavController, viewModel: TimetableViewModel = hiltViewModel()) {

    val fredoka = rememberFredoka()
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(timetable = true, onClick = {
                viewModel.bottomNav(it, currentScreen = Screen.TimetableScreen)
            })
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                TimetableTitle(fredoka = fredoka)
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
            }
            item {
                TimetableDays(fredoka = fredoka, scrollState = horizontalScrollState)
            }
            items(11) {
                TimetableSubjectRow(
                    hour = it + 1,
                    scrollState = horizontalScrollState,
                    fredoka = fredoka
                )
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 50.dp))
            }
        }
    }
}

@Composable
fun TimetableTitle(fredoka: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Timetable",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
        IconButton(onClick = {  }) {
            Icon(Icons.Default.Filter, contentDescription = "", modifier = Modifier.scale(1.2f))
        }
    }
}

@Composable
fun TimetableDays(fredoka: FontFamily, scrollState: ScrollState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(state = scrollState)
            .padding()
    ) {
        TimetableDayText(text = "", fredoka = fredoka, weight = .1f)
        TimetableDayText(text = "Monday", fredoka = fredoka, weight = .3f)
        TimetableDayText(text = "Tuesday", fredoka = fredoka, weight = .3f)
        TimetableDayText(text = "Wednesday", fredoka = fredoka, weight = .3f)
        TimetableDayText(text = "Thursday", fredoka = fredoka, weight = .3f)
        TimetableDayText(text = "Friday", fredoka = fredoka, weight = .3f)
    }
}

@Composable
fun RowScope.TimetableDayText(text: String, fredoka: FontFamily, weight: Float) {
    Text(
        text = text,
        fontFamily = fredoka,
        fontWeight = FontWeight.Light,
        color = Color.Gray,
        modifier = Modifier
            .weight(weight)
            .padding(end = 5.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TimetableSubjectRow(hour: Int, scrollState: ScrollState, fredoka: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimetableHourText(hour = hour.toString(), weight = .1f, fredoka = fredoka)
        TimetableSubjectItem(
            color = Color.Green,
            weight = .3f,
            subject = "Mathematics",
            room = "423"
        )
        TimetableSubjectItem(color = Color.Yellow, weight = .3f, subject = "German", room = "4354")
        TimetableSubjectItem(color = Color.Cyan, weight = .3f, subject = "History", room = "654")
        TimetableSubjectItem(color = Color.Magenta, weight = .3f, subject = "English", room = "443")
        TimetableSubjectItem(
            color = Color.LightGray,
            weight = .3f,
            subject = "Economy",
            room = "32"
        )
    }
}

@Composable
fun RowScope.TimetableHourText(hour: String, weight: Float, fredoka: FontFamily) {
    Text(
        text = hour,
        modifier = Modifier
            .weight(weight)
            .padding()
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.Gray,
        fontFamily = fredoka,
        fontWeight = FontWeight.Light
    )
}

@Composable
fun RowScope.TimetableSubjectItem(color: Color, weight: Float, subject: String, room: String) {
    Card(
        backgroundColor = color,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .weight(weight)
            .padding(5.dp)
            .clickable {

            }
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Column {
                Text(
                    text = subject,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = room,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

