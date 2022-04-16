package com.mnowo.offlineschoolmanager

import android.util.Log.d
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.*
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.rememberWindowInfo
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.AddSubjectEvent
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker.PickColorEvent
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen.SubjectEvent
import com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen.SubjectViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectScreen(navController: NavController, viewModel: SubjectViewModel = hiltViewModel()) {

    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()
    val scope = rememberCoroutineScope()

    val bottomState = rememberBottomSheetScaffoldState()

    val closeSheet: () -> Unit = {
        scope.launch {
            bottomState.bottomSheetState.collapse()
        }
    }

    val openSheet: () -> Unit = {
        scope.launch {
            bottomState.bottomSheetState.expand()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Constants.GRADE_NAV_ARGUMENT,
                        viewModel.onSubjectListClickedIndexState.value
                    )

                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }

    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        scaffoldState = bottomState,
        sheetContent = {
            AddSubjectBottomSheet(
                onCloseBottomSheet = closeSheet,
                fredoka = fredoka,
                scaffoldState = bottomState
            )
        },
        sheetElevation = 5.dp
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(gradeAverage = true, onClick = {
                    viewModel.bottomNav(it, currentScreen = Screen.SubjectScreen)
                })
            }
        ) {
            LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                item {
                    SubjectTitle(fredoka = fredoka, onOpenBottomSheet = {
                        openSheet()
                    })
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                }
                items(viewModel.subjectListState.value.listData) {
                    SubjectListItem(
                        fredoka = fredoka,
                        data = it,
                        onSubjectItemClicked = { index ->
                            viewModel.onEvent(SubjectEvent.OnSubjectClicked(index))
                        })
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 0.8.dp
                    )
                }

            }
        }
    }
}

@Composable
fun SubjectTitle(fredoka: FontFamily, onOpenBottomSheet: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Subjects",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                onOpenBottomSheet()
            }) {
                Icon(Icons.Default.Add, contentDescription = "", modifier = Modifier.scale(1.2f))
            }

            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "",
                    modifier = Modifier.scale(1.2f)
                )
            }
        }


    }
}

@Composable
fun SubjectListItem(fredoka: FontFamily, data: Subject, onSubjectItemClicked: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSubjectItemClicked(data.id)
            }
            .padding(15.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = Color(
                        red = data.color.red,
                        green = data.color.green,
                        blue = data.color.blue
                    )
                )
        )
        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = data.subjectName, fontFamily = fredoka, fontWeight = FontWeight.Light)
            Text(
                text = data.average.toString(),
                fontFamily = fredoka,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
    }
}