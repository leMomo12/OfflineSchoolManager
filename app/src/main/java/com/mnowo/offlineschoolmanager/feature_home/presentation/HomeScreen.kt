package com.mnowo.offlineschoolmanager

import android.util.Log.d
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.WindowInfo
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.rememberWindowInfo
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_home.presentation.HomeViewModel
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import kotlinx.coroutines.flow.collectLatest


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavController) {

    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()

    val listState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        viewModel.getStartingInformation()

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
            BottomAppBar(home = true, onClick = {
                viewModel.bottomNav(it, currentScreen = Screen.HomeScreen)
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {

            LazyColumn(state = listState) {
                item {
                    HomeScreenTitle(
                        windowInfo = windowInfo,
                        fredoka = fredoka,
                        viewModel = viewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    HomeGradeAverage(
                        windowInfo = windowInfo,
                        viewModel = viewModel,
                        fredoka = fredoka
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(15.dp))
                    HomeTodayTimetable(fredoka = fredoka, windowInfo = windowInfo, viewModel = viewModel)
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    HomeUpcomingExams(fredoka = fredoka, windowInfo = windowInfo)
                }
                items(3) {
                    UpcomingExamsItem(fredoka = fredoka, windowInfo = windowInfo)
                }
                item {
                    Spacer(modifier = Modifier.padding(60.dp))
                }
            }

        }
    }
}

@Composable
fun HomeScreenTitle(windowInfo: WindowInfo, fredoka: FontFamily, viewModel: HomeViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Overview",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 40.sp
        )
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Settings, contentDescription = "")
            }
        }
    }
    Text(
        text = viewModel.currentTimeState.value,
        fontFamily = fredoka,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Gray
    )
}

@Composable
fun HomeGradeAverage(windowInfo: WindowInfo, viewModel: HomeViewModel, fredoka: FontFamily) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(180.dp),
            border = BorderStroke(2.5.dp, color = viewModel.gradeColorState.value),
            modifier = Modifier
                .size(windowInfo.screenWidth / 2 - 20.dp),
            elevation = 0.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.averageState.value.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 45.sp,
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun HomeTodayTimetable(fredoka: FontFamily, windowInfo: WindowInfo, viewModel: HomeViewModel) {
    Text(
        text = "Today's timetable",
        fontFamily = fredoka,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp
    )
    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    LazyRow {
        items(viewModel.dailyTimetableListState.value.listData) {
            HomeTimetableRow(
                timetable = it,
                fredoka = fredoka,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun HomeTimetableRow(timetable: Timetable, fredoka: FontFamily, viewModel: HomeViewModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        border = BorderStroke(width = 1.dp, color = Color(viewModel.dailyTimetableMap[timetable]?.color ?: 0) )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(end = 15.dp, start = 15.dp, top = 20.dp, bottom = 20.dp)
        ) {
            Text(text = "${timetable.hour}. ${viewModel.dailyTimetableMap[timetable]?.subjectName} ", fontSize = 20.sp, fontFamily = FontFamily.SansSerif)
            Text(
                text = "${viewModel.dailyTimetableMap[timetable]?.room}",
                fontFamily = fredoka,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun HomeUpcomingExams(fredoka: FontFamily, windowInfo: WindowInfo) {
    Text(
        text = "Upcoming exams",
        fontFamily = fredoka,
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun UpcomingExamsItem(fredoka: FontFamily, windowInfo: WindowInfo) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        elevation = 5.dp,
        border = BorderStroke(0.4.dp, color = Color.LightGray)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, bottom = 24.dp, top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "English Exam",
                fontFamily = fredoka,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
            Text(
                text = "13 December 2022",
                fontFamily = fredoka,
                fontWeight = FontWeight.Light,
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}
