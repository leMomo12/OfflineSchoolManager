package com.mnowo.offlineschoolmanager

import android.util.Log.d
import android.util.Log.i
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dropdown_menu.EditAndDeleteDropdownMenu
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_exam.presentation.ExamBottomSheet
import com.mnowo.offlineschoolmanager.feature_exam.presentation.ExamEvent
import com.mnowo.offlineschoolmanager.feature_exam.presentation.ExamViewModel
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.Untainted

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExamScreen(navController: NavController, viewModel: ExamViewModel = hiltViewModel()) {

    val fredoka = rememberFredoka()
    val bottomState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = Constants.GRADE_NAV_ARGUMENT,
                        value = viewModel.addExamSubjectIdState.value
                    )

                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }

    val openSheet: () -> Unit = {
        scope.launch {
            viewModel.removeAllErrors()
            viewModel.clearAfterExamEvent()
            bottomState.bottomSheetState.expand()
        }
    }

    val closeSheet: () -> Unit = {
        scope.launch {
            bottomState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        sheetContent = {
            ExamBottomSheet(
                viewModel = viewModel,
                onCloseBottomSheet = { closeSheet() },
                fredoka = fredoka
            )
        },
        sheetElevation = 5.dp
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(exam = true, onClick = {
                    viewModel.bottomNav(it, currentScreen = Screen.ExamScreen)
                })
            }
        ) {
            LazyColumn() {
                item {
                    ExamTitle(
                        fredoka = fredoka,
                        openSheet = { openSheet() },
                        bottomState = bottomState,
                        viewModel = viewModel
                    )
                }
                items(viewModel.examListState.value.listData) {
                    ExamItem(
                        viewModel = viewModel,
                        examData = it,
                        fredoka = fredoka,
                        openSheet = { openSheet() })
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 50.dp))
                }
            }

            if (viewModel.examListState.value.listData.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(id = R.drawable.exam_icon), contentDescription = "")
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Text(
                        text = stringResource(R.string.examEmptyListTitle),
                        fontFamily = fredoka,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource(R.string.examEmptyListSubtitle),
                        fontFamily = fredoka,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(top = 5.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExamTitle(
    fredoka: FontFamily,
    openSheet: () -> Unit,
    bottomState: BottomSheetScaffoldState,
    viewModel: ExamViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.exam),
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (!viewModel.editState.value && !viewModel.deleteState.value) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(ExamEvent.ChangeBottomSheetState(true))
                        openSheet()
                    },
                    enabled = bottomState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )
                }
                IconButton(
                    onClick = { viewModel.onEvent(ExamEvent.SetDropDownMenuState(true)) },
                    enabled = bottomState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )

                    EditAndDeleteDropdownMenu(
                        fredoka = fredoka,
                        expanded = viewModel.dropDownMenuState.value,
                        onDismissRequest = { viewModel.onEvent(ExamEvent.SetDropDownMenuState(false)) },
                        onEditMenuClicked = { viewModel.onEvent(ExamEvent.SetEditState(true)) },
                        editMenuEnabled = bottomState.bottomSheetState.isCollapsed,
                        onDeleteMenuClicked = { viewModel.onEvent(ExamEvent.SetDeleteState(true)) },
                        deleteMenuEnabled = bottomState.bottomSheetState.isCollapsed
                    )
                }
            } else {
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(ExamEvent.SetEditState(false))
                        viewModel.onEvent(ExamEvent.SetDeleteState(false))
                        viewModel.onEvent(ExamEvent.SetDropDownMenuState(false))
                    },
                    border = BorderStroke(1.4.dp, color = LightBlue),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .padding(end = 5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        fontFamily = fredoka,
                        color = LightBlue
                    )
                }
            }
        }
    }
}

@Composable
fun ExamItem(viewModel: ExamViewModel, examData: Exam, fredoka: FontFamily, openSheet: () -> Unit) {
    val subjectState by remember {
        derivedStateOf {
            viewModel.getSubjectItem(examData = examData)
        }
    }

    val isExpiredState by remember {
        derivedStateOf {
            viewModel.isExamExpired(examLongDate = examData.date)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color(
                    red = subjectState.color.red,
                    green = subjectState.color.green,
                    blue = subjectState.color.blue,
                    alpha = subjectState.color.alpha
                )
            )
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 15.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = subjectState.subjectName + " " + stringResource(R.string.exam),
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Light,
                    color = Color.DarkGray
                )
            }
            Text(text = examData.title, fontFamily = fredoka, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Text(text = examData.description, fontFamily = fredoka, fontWeight = FontWeight.Normal)

            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.onEvent(ExamEvent.AddResult(subjectId = examData.subjectId)) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            red = subjectState.color.red,
                            green = subjectState.color.green,
                            blue = subjectState.color.blue,
                            alpha = subjectState.color.alpha
                        )
                    ),
                    enabled = isExpiredState
                ) {
                    Text(text = "Add result")
                }
                Text(
                    text = FormatDate.formatLongToSpring(time = examData.date),
                    color = Color.DarkGray,
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Light
                )
            }
            if (isExpiredState) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.expired),
                        fontFamily = fredoka,
                        color = Color.Red
                    )
                    Icon(
                        Icons.Outlined.ErrorOutline,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
            when {
                viewModel.editState.value -> {
                    ExamDeleteOrEditState(isEditState = true, onClick = {
                        viewModel.onEvent(ExamEvent.SetEditSpecificExam(exam = examData))
                        viewModel.onEvent(ExamEvent.SetPickedSubjectState(subject = subjectState))
                        viewModel.onEvent(ExamEvent.SetContentEditState(true))
                        viewModel.onEvent(ExamEvent.ChangeBottomSheetState(true))
                        openSheet()
                    })
                }
                viewModel.deleteState.value -> {
                    ExamDeleteOrEditState(isEditState = false, onClick = {
                        viewModel.onEvent(ExamEvent.DeleteExam(id = examData.id))
                    })
                }
            }
        }
    }
}

@Composable
fun ExamDeleteOrEditState(isEditState: Boolean, onClick: () -> Unit) {
    Divider()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { onClick() }) {
            if (isEditState) {
                Icon(Icons.Default.Edit, contentDescription = "")
            } else {
                Icon(Icons.Default.Delete, contentDescription = "")
            }
        }
    }
}
