package com.mnowo.offlineschoolmanager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.lights.Light
import android.util.Log.d
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.WindowInfo
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.rememberWindowInfo
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.core.theme.darkerWhite
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_home.presentation.CustomAverageCircle
import com.mnowo.offlineschoolmanager.feature_home.presentation.HomeEvent
import com.mnowo.offlineschoolmanager.feature_home.presentation.HomeViewModel
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(), navController: NavController
) {

    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()

    val listState = rememberLazyListState()
    val context = LocalContext.current

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
                        viewModel = viewModel,
                        context = context
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
                    Spacer(modifier = Modifier.padding(10.dp))
                    HomeTodayTimetable(
                        fredoka = fredoka,
                        windowInfo = windowInfo,
                        viewModel = viewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    HomeUpcomingExams(
                        fredoka = fredoka,
                        windowInfo = windowInfo,
                        viewModel = viewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(60.dp))
                }
            }

        }
    }
}

@Composable
fun HomeScreenTitle(
    windowInfo: WindowInfo,
    fredoka: FontFamily,
    viewModel: HomeViewModel,
    context: Context
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.Overwiew),
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 40.sp
        )
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_LONG).show()
            }) {
                Icon(Icons.Default.Notifications, contentDescription = "")
            }
            IconButton(onClick = {
                viewModel.onEvent(HomeEvent.NavigateToSettingsScreen)
            }) {
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
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(windowInfo.screenWidth / 1 - 20.dp)
                .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing)),
            elevation = 1.dp,
            backgroundColor = darkerWhite,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.yourAverage),
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomAverageCircle(
                        fredoka = fredoka,
                        foregroundIndicatorColor = viewModel.gradeColorState.value,
                        indicatorValue = 6 - viewModel.averageState.value + 1,
                        indicatorText = "${viewModel.averageState.value}",
                        smallText = stringResource(R.string.average)
                    )
                }
                HomeAverageDropdown(
                    fredoka = fredoka,
                    bestSubject = viewModel.bestAndWorstSubjectState[0]
                        ?: stringResource(R.string.noData),
                    worstSubject = viewModel.bestAndWorstSubjectState[1]
                        ?: stringResource(id = R.string.noData)
                )
            }
        }
    }
}

@Composable
fun HomeAverageDropdown(fredoka: FontFamily, bestSubject: String, worstSubject: String) {
    val dropdownState = remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(onClick = { dropdownState.value = !dropdownState.value }) {
            if (dropdownState.value) {
                Icon(Icons.Default.ArrowDropUp, contentDescription = "")
            } else {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
            }
        }
    }
    if (dropdownState.value) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            HomeBestSubject(fredoka = fredoka, bestSubject = bestSubject)
            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
            )
            HomeWorstSubject(fredoka = fredoka, worstSubject = worstSubject)
        }
    }
}

@Composable
fun RowScope.HomeBestSubject(fredoka: FontFamily, bestSubject: String) {
    Column(
        modifier = Modifier
            .weight(0.5f)
            .padding(end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.best), fontFamily = fredoka, color = Color.Gray)
        Text(
            text = bestSubject,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = fredoka
        )
    }
}

@Composable
fun RowScope.HomeWorstSubject(fredoka: FontFamily, worstSubject: String) {
    Column(
        modifier = Modifier
            .weight(0.5f)
            .padding(start = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.worst), fontFamily = fredoka, color = Color.Gray)
        Text(
            text = worstSubject,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = fredoka
        )
    }
}


@Composable
fun HomeAverageCircle(windowInfo: WindowInfo, viewModel: HomeViewModel, fredoka: FontFamily) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(180.dp),
            border = BorderStroke(2.5.dp, color = viewModel.gradeColorState.value),
            modifier = Modifier
                .size(windowInfo.screenWidth / 2 - 40.dp),
            elevation = 0.dp,
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
    Card(
        modifier = Modifier
            .width(windowInfo.screenWidth / 1 - 20.dp),
        elevation = 2.dp,
        backgroundColor = darkerWhite,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (viewModel.isTodayTimetableState.value) {
                        stringResource(id = R.string.todayTimetable)
                    } else {
                        stringResource(R.string.nextTimetable)
                    },
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                IconButton(onClick = {
                    viewModel.bottomNav(Screen.TimetableScreen, Screen.HomeScreen)
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            LazyRow(modifier = Modifier.fillMaxSize()) {
                items(viewModel.dailyTimetableListState.value.listData) {
                    HomeTimetableRow(
                        timetable = it,
                        fredoka = fredoka,
                        viewModel = viewModel
                    )
                }
            }
            if (viewModel.emptyDailyList.value) {
                HomeNoDataYet(
                    title = stringResource(id = R.string.youHaveNoTimetableYet),
                    description = stringResource(
                        id = R.string.letsChangeThat
                    ),
                    fredoka = fredoka
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}

@Composable
fun HomeTimetableRow(timetable: Timetable, fredoka: FontFamily, viewModel: HomeViewModel) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeTimetableHourLine(hour = timetable.hour)
        }
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        HomeTimetableItem(viewModel = viewModel, fredoka = fredoka, timetable = timetable)
    }
}

@Composable
fun HomeTimetableHourLine(hour: Int) {
    Divider(
        modifier = Modifier.width(70.dp),
        color = Color.LightGray,
        thickness = 0.8.dp
    )
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = hour.toString(),
            modifier = Modifier.padding(start = 2.dp, end = 2.dp)
        )
    }
    Divider(
        modifier = Modifier.width(70.dp),
        color = Color.LightGray,
        thickness = 0.8.dp
    )
}

@Composable
fun HomeTimetableItem(viewModel: HomeViewModel, fredoka: FontFamily, timetable: Timetable) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp,
        modifier = Modifier
            .widthIn(min = 60.dp, max = 150.dp)
            .padding(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(viewModel.dailyTimetableMap[timetable]?.color ?: 0)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 10.dp, start = 10.dp, top = 15.dp, bottom = 15.dp)
        ) {
            Text(
                text = "${viewModel.dailyTimetableMap[timetable]?.subjectName}",
                fontSize = 18.sp,
                fontFamily = fredoka,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${viewModel.dailyTimetableMap[timetable]?.room}",
                fontFamily = fredoka,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun HomeUpcomingExams(fredoka: FontFamily, windowInfo: WindowInfo, viewModel: HomeViewModel) {
    Card(
        modifier = Modifier
            .width(windowInfo.screenWidth / 1 - 20.dp),
        elevation = 2.dp,
        backgroundColor = darkerWhite,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.upcomingExams),
                    fontFamily = fredoka,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Visible,
                    maxLines = 1
                )
                IconButton(onClick = {
                    viewModel.bottomNav(
                        Screen.ExamScreen,
                        Screen.HomeScreen
                    )
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "")
                }
            }
            //Spacer(modifier = Modifier.padding(vertical = 10.dp))

            viewModel.notExpiredExamListState.value.listData.take(3).forEach { examData ->
                UpcomingExamsItem(
                    fredoka = fredoka,
                    windowInfo = windowInfo,
                    examData = examData,
                    viewModel = viewModel
                )
            }
            if (viewModel.notExpiredExamListState.value.listData.isEmpty()) {
                HomeNoDataYet(
                    title = stringResource(R.string.youHaveNotUpcomingExams),
                    description = stringResource(id = R.string.letsChangeThat),
                    fredoka = fredoka
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun UpcomingExamsItem(
    fredoka: FontFamily,
    windowInfo: WindowInfo,
    examData: Exam,
    viewModel: HomeViewModel
) {
    val subjectState by remember {
        derivedStateOf {
            viewModel.getExamSubjectItem(examData = examData)
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 8.dp, bottom = 8.dp),
            elevation = 5.dp,
            border = BorderStroke(
                0.4.dp, color = Color(
                    red = subjectState.color.red,
                    green = subjectState.color.green,
                    blue = subjectState.color.blue,
                    alpha = subjectState.color.alpha
                )
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = examData.title,
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Text(
                    text = FormatDate.formatLongToSpring(time = examData.date),
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun HomeNoDataYet(title: String, description: String, fredoka: FontFamily) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = description,
            fontFamily = fredoka,
            color = Color.Gray
        )
    }
}
