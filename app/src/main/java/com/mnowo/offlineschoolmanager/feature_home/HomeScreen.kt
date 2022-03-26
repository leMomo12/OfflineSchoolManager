package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.mnowo.composesurveyapp.core.presentation.util.UiEvent
import com.mnowo.offlineschoolmanager.core.WindowInfo
import com.mnowo.offlineschoolmanager.core.rememberWindowInfo
import com.mnowo.offlineschoolmanager.feature_home.presentation.HomeViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavController) {

    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()

    val listState = rememberLazyListState()

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
            BottomAppBar(home = true, onClick = {
                viewModel.bottomNav(it)
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
                    HomeScreenTitle(windowInfo = windowInfo, fredoka = fredoka)
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    HomeGradeAverage(windowInfo = windowInfo)
                }
                item {
                    Spacer(modifier = Modifier.padding(15.dp))
                    HomeTodayTimetable(fredoka = fredoka, windowInfo = windowInfo)
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
fun HomeScreenTitle(windowInfo: WindowInfo, fredoka: FontFamily) {
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
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
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.Notifications, contentDescription = "")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Settings, contentDescription = "")
                }
            }
        }
        Text(
            text = "10 December 2022",
            fontFamily = fredoka,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Gray
        )
    } else {
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            text = "Dashboard",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 64.sp
        )
        Text(
            text = "10 December 2022",
            fontFamily = fredoka,
            fontWeight = FontWeight.Normal,
            fontSize = 26.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun HomeGradeAverage(windowInfo: WindowInfo) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(180.dp),
            border = BorderStroke(2.dp, color = Color.Green),
            modifier = Modifier
                .size(windowInfo.screenWidth / 2 - 20.dp),
            elevation = 0.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "2.1",
                    textAlign = TextAlign.Center,
                    fontSize = 45.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun HomeTodayTimetable(fredoka: FontFamily, windowInfo: WindowInfo) {
    Text(
        text = "Today's timetable",
        fontFamily = fredoka,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp
    )
    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    LazyRow {
        items(6) {
            HomeTimetableRow(
                windowInfo = windowInfo,
                hour = it,
                fredoka = fredoka,
                borderColor = Color.Blue
            )
        }
    }
}

@Composable
fun HomeTimetableRow(windowInfo: WindowInfo, hour: Int, fredoka: FontFamily, borderColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        border = BorderStroke(width = 0.5.dp, borderColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(end = 15.dp, start = 15.dp, top = 20.dp, bottom = 20.dp)
        ) {
            Text(text = "$hour. ", fontSize = 20.sp, fontFamily = FontFamily.SansSerif)
            Text(
                text = "English",
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
