package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.feature_exam.presentation.ExamBottomSheet
import com.mnowo.offlineschoolmanager.feature_exam.presentation.ExamEvent
import com.mnowo.offlineschoolmanager.feature_exam.presentation.ExamViewModel
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoBottomSheet
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }

    val openSheet: () -> Unit = {
        scope.launch {
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
            text = "Exam",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
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
            IconButton(onClick = {
            }, enabled = bottomState.bottomSheetState.isCollapsed) {
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = "",
                    modifier = Modifier.scale(1.2f)
                )

                DropdownMenu(
                    expanded = false,
                    onDismissRequest = {

                    },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .testTag(GradeTestTags.DROPDOWN_MENU)
                ) {
                    DropdownMenuItem(
                        onClick = { },
                        modifier = Modifier.testTag(GradeTestTags.EDIT_MENU_ITEM),
                        enabled = bottomState.bottomSheetState.isCollapsed
                    ) {
                        Row {
                            Icon(Icons.Default.Edit, contentDescription = "")
                            Text(
                                text = stringResource(id = R.string.edit),
                                fontFamily = fredoka,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }
                    }
                    DropdownMenuItem(
                        onClick = { },
                        modifier = Modifier.testTag(GradeTestTags.DELETE_MENU_ITEM),
                        enabled = bottomState.bottomSheetState.isCollapsed
                    ) {
                        Row {
                            Icon(Icons.Default.Delete, contentDescription = "")
                            Text(
                                text = stringResource(id = R.string.delete),
                                fontFamily = fredoka,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
