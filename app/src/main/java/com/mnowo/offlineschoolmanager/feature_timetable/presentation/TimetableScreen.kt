package com.mnowo.offlineschoolmanager

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.DeleteDialog
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Days
import com.mnowo.offlineschoolmanager.feature_timetable.domain.models.Timetable
import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableBottomSheet
import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableEvent
import com.mnowo.offlineschoolmanager.feature_timetable.presentation.TimetableViewModel
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableScreen(navController: NavController, viewModel: TimetableViewModel = hiltViewModel()) {

    val fredoka = rememberFredoka()
    val horizontalScrollState = rememberScrollState()
    val bottomState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val closeSheet: () -> Unit = {
        coroutineScope.launch {
            bottomState.bottomSheetState.collapse()
        }
    }

    val openSheet: () -> Unit = {
        coroutineScope.launch {
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
            TimetableBottomSheet(viewModel = viewModel, fredoka = fredoka, onCloseBottomSheet = {
                closeSheet()
            })
        },
        sheetElevation = 5.dp
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(timetable = true, onClick = {
                    viewModel.bottomNav(it, currentScreen = Screen.TimetableScreen)
                })
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    TimetableTitle(
                        fredoka = fredoka,
                        onOpenBottomSheet = { openSheet() },
                        bottomSheetState = bottomState,
                        viewModel = viewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                }
                item {
                    TimetableDays(fredoka = fredoka, scrollState = horizontalScrollState)
                }
                items(12) {
                    TimetableSubjectRow(
                        hour = it + 1,
                        scrollState = horizontalScrollState,
                        fredoka = fredoka,
                        viewModel = viewModel,
                        openSheet = { openSheet() }
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 50.dp))
                }
            }

            if (viewModel.deleteAllItemsState.value) {
                DeleteDialog(
                    onDismissRequest = {
                        viewModel.onEvent(
                            TimetableEvent.SetDeleteAllItemsState(
                                false
                            )
                        )
                    },
                    onDeleteClicked = { viewModel.onEvent(TimetableEvent.DeleteEntireTimetable) },
                    title = stringResource(R.string.sureToDeleteAllTimetableItems),
                    text = stringResource(id = R.string.thisChangeCannotBeReset)
                )
            }
            if (viewModel.deleteDialogState.value) {
                DeleteDialog(
                    onDismissRequest = { viewModel.onEvent(TimetableEvent.SetDeleteDialogState(false)) },
                    onDeleteClicked = { viewModel.onEvent(TimetableEvent.DeleteTimetableItem) },
                    title = stringResource(id = R.string.sureToDelete),
                    text = stringResource(id = R.string.thisChangeCannotBeReset)
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableTitle(
    fredoka: FontFamily,
    onOpenBottomSheet: () -> Unit,
    bottomSheetState: BottomSheetScaffoldState,
    viewModel: TimetableViewModel
) {
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
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!viewModel.editState.value && !viewModel.deleteState.value && !viewModel.deleteAllItemsState.value) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(TimetableEvent.SetTimetableBottomSheet(true))
                        onOpenBottomSheet()
                    },
                    enabled = bottomSheetState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.onEvent(TimetableEvent.SetDropdownMenuState(true))
                    },
                    enabled = bottomSheetState.bottomSheetState.isCollapsed
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "", modifier = Modifier.scale(1.2f))
                }

                DropdownMenu(
                    expanded = viewModel.dropdownMenuState.value,
                    onDismissRequest = {
                        viewModel.onEvent(TimetableEvent.SetDropdownMenuState(false))
                    },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                ) {
                    DropdownMenuItem(
                        onClick = { viewModel.onEvent(TimetableEvent.SetEditState(true)) },
                        enabled = bottomSheetState.bottomSheetState.isCollapsed
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
                        onClick = { viewModel.onEvent(TimetableEvent.SetDeleteState(true)) },
                        enabled = bottomSheetState.bottomSheetState.isCollapsed
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
                    DropdownMenuItem(
                        onClick = { viewModel.onEvent(TimetableEvent.SetDeleteAllItemsState(true)) },
                        enabled = bottomSheetState.bottomSheetState.isCollapsed
                    ) {
                        Row {
                            Icon(Icons.Default.DeleteForever, contentDescription = "")
                            Text(
                                text = "Delete all timetable items",
                                fontFamily = fredoka,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(TimetableEvent.SetEditState(false))
                        viewModel.onEvent(TimetableEvent.SetDeleteState(false))
                        viewModel.onEvent(TimetableEvent.SetDeleteAllItemsState(false))
                        viewModel.onEvent(TimetableEvent.SetDropdownMenuState(false))
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
fun TimetableDays(fredoka: FontFamily, scrollState: ScrollState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(state = scrollState)
    ) {
        TimetableDayText(text = "", fredoka = fredoka, weight = .15f)
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
fun TimetableSubjectRow(
    hour: Int,
    scrollState: ScrollState,
    fredoka: FontFamily,
    viewModel: TimetableViewModel,
    openSheet: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimetableHourText(hour = hour, weight = .1f, fredoka = fredoka)

        if (viewModel.timetableListState.value.listData.isNotEmpty()) {
            for (day in 0 until 5) {
                val timetable = viewModel.searchIfTimetableItemExists(hour = hour, intDay = day)
                val subject = viewModel.searchForSubject(
                    subjectId = timetable.subjectId,
                    timetableId = timetable.id
                )
                TimetableSubjectItem(
                    color = Color(subject.color.red, subject.color.green, subject.color.blue),
                    weight = .3f,
                    subject = subject.subjectName,
                    room = subject.room ?: "",
                    timetable = timetable,
                    viewModel = viewModel,
                    openSheet = { openSheet() },
                    fredoka = fredoka
                )
            }

        } else {
            for (day in 0 until 5) {
                TimetableSubjectItem(
                    color = Color.LightGray,
                    weight = .3f,
                    subject = "",
                    room = "",
                    timetable = Timetable(-1, Days.EXCEPTION, 0, -1),
                    viewModel = viewModel,
                    openSheet = { openSheet() },
                    fredoka = fredoka
                )
            }
        }
    }
}

@Composable
fun RowScope.TimetableHourText(hour: Int, weight: Float, fredoka: FontFamily) {
    Text(
        text = hour.toString(),
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
fun RowScope.TimetableSubjectItem(
    color: Color,
    weight: Float,
    subject: String,
    room: String,
    timetable: Timetable,
    viewModel: TimetableViewModel,
    openSheet: () -> Unit,
    fredoka: FontFamily
) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = fredoka,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutLinearInEasing
                            )
                        ),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    when {
                        (!viewModel.deleteState.value && !viewModel.editState.value) || (color == Color.LightGray) -> {
                            Text(
                                text = room,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Light
                            )
                        }
                        viewModel.editState.value && color != Color.LightGray -> {
                            IconButton(onClick = {
                                viewModel.onEvent(
                                    TimetableEvent.SetTimetableSpecificItem(
                                        timetable = timetable
                                    )
                                )

                                viewModel.onEvent(
                                    TimetableEvent.OnEditClicked(
                                        timetableItem = timetable
                                    )
                                )

                                openSheet()
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "",
                                    modifier = Modifier.scale(0.9f)
                                )
                            }
                        }
                        viewModel.deleteState.value && color != Color.LightGray -> {
                            IconButton(onClick = {

                                viewModel.onEvent(
                                    TimetableEvent.SetTimetableSpecificItem(timetable = timetable)
                                )

                                viewModel.onEvent(
                                    TimetableEvent.SetDeleteDialogState(true)
                                )

                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                    modifier = Modifier.scale(0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
