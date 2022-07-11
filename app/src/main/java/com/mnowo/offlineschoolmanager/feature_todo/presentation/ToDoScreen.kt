package com.mnowo.offlineschoolmanager

import android.util.Log.d
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.DeleteDialog
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoBottomSheet
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoScreen(navController: NavController, viewModel: ToDoViewModel = hiltViewModel()) {
    val fredoka = rememberFredoka()
    val bottomState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

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
                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        sheetContent = {
            ToDoBottomSheet(onCloseBottomSheet = { closeSheet() }, viewModel = viewModel)
        },
        sheetElevation = 5.dp
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(toDo = true, onClick = {
                    viewModel.bottomNav(it, currentScreen = Screen.ToDoScreen)
                })
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    ToDoTitle(
                        fredoka = fredoka,
                        openSheet = { openSheet() },
                        viewModel = viewModel,
                        bottomSheetScaffoldState = bottomState
                    )
                }
                item {
                    ToDoStaggeredGrid(
                        listData = viewModel.toDoList.value.listData,
                        fredoka = fredoka,
                        viewModel = viewModel,
                        openSheet = { openSheet() }
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 60.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoTitle(
    fredoka: FontFamily,
    openSheet: () -> Unit,
    viewModel: ToDoViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.toDoS),
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (!viewModel.editState.value && !viewModel.deleteState.value) {
                IconButton(
                    onClick = { openSheet() },
                    enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )
                }
                IconButton(onClick = {
                    viewModel.onEvent(ToDoEvent.ChangeDropDownMenuState(!viewModel.dropDownMenuState.value))
                }, enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )

                    DropdownMenu(
                        expanded = viewModel.dropDownMenuState.value,
                        onDismissRequest = {
                            viewModel.onEvent(
                                ToDoEvent.ChangeDropDownMenuState(
                                    false
                                )
                            )
                        },
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .testTag(GradeTestTags.DROPDOWN_MENU)
                    ) {
                        DropdownMenuItem(
                            onClick = { viewModel.onEvent(ToDoEvent.ChangeEditState(true)) },
                            modifier = Modifier.testTag(GradeTestTags.EDIT_MENU_ITEM),
                            enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed
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
                            onClick = { viewModel.onEvent(ToDoEvent.ChangeDeleteState(true)) },
                            modifier = Modifier.testTag(GradeTestTags.DELETE_MENU_ITEM),
                            enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed
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
            } else {
                IconButton(onClick = { }, enabled = false) {
                    Icon(Icons.Default.MoreVert, contentDescription = "", tint = Color.Transparent)
                }
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(ToDoEvent.ChangeEditState(false))
                        viewModel.onEvent(ToDoEvent.ChangeDeleteState(false))
                        viewModel.onEvent(ToDoEvent.ChangeDropDownMenuState(false))
                    },
                    border = BorderStroke(1.4.dp, color = LightBlue),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .testTag(GradeTestTags.CANCEL_BUTTON)
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
fun ToDoStaggeredGrid(
    listData: List<ToDo>,
    fredoka: FontFamily,
    viewModel: ToDoViewModel,
    openSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        StaggeredVerticalGrid(
            numColumns = 2, //put the how many column you want
            modifier = Modifier.padding(5.dp)
        ) {
            listData.forEach { item ->

                if (viewModel.deleteDialogState.value) {
                    DeleteDialog(onDismissRequest = {
                        viewModel.onEvent(
                            ToDoEvent.ChangeDeleteDialogState(
                                value = false
                            )
                        )
                    }, onDeleteClicked = {
                        d("ToDo", "onDeleteClicked: ${viewModel.deleteToDoIdState.value}")
                        viewModel.onEvent(ToDoEvent.DeleteToDo)
                    },
                        title = stringResource(id = R.string.sureToDelete),
                        text = stringResource(id = R.string.thisChangeCannotBeReset)
                    )
                }

                val colorState = remember {
                    derivedStateOf {
                        if (viewModel.subjectList.value.listData.isNotEmpty()) {
                            val intColor =
                                viewModel.subjectList.value.listData.filter { it.id == item.subjectId }[0].color
                            return@derivedStateOf Color(intColor)
                        } else {
                            return@derivedStateOf Color.LightGray
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = colorState.value)
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 600,
                                easing = LinearOutSlowInEasing
                            )
                        )
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = {
                                    viewModel.onEvent(
                                        ToDoEvent.OnCheckboxChanged(
                                            toDoId = item.id,
                                            newValue = !item.isChecked
                                        )
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                            Text(
                                text = "Until: ${FormatDate.formatLongToSpring(item.until)}",
                                fontFamily = fredoka,
                                fontWeight = FontWeight.Light
                            )

                        }
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            ),
                            fontFamily = fredoka,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = item.description,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            fontFamily = fredoka
                        )

                        when {
                            viewModel.editState.value -> {
                                Divider()
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = {
                                            viewModel.setContentEditState(value = true)
                                            viewModel.onEvent(
                                                ToDoEvent.ChangeSpecificEditToDoState(
                                                    value = item
                                                )
                                            )
                                            viewModel.onEvent(
                                                ToDoEvent.ChangeEditState(
                                                    value = true
                                                )
                                            )
                                            openSheet()
                                        },
                                        modifier = Modifier
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "")
                                    }
                                }
                            }
                            viewModel.deleteState.value -> {
                                Divider()
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = {
                                            viewModel.setDeleteToDoIdState(value = item.id)
                                            viewModel.onEvent(
                                                ToDoEvent.ChangeDeleteDialogState(
                                                    value = true
                                                )
                                            )
                                        },
                                        modifier = Modifier
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
