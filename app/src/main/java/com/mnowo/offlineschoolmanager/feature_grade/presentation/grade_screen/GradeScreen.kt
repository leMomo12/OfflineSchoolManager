package com.mnowo.offlineschoolmanager

import android.util.Log.d
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.rememberWindowInfo
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.DeleteDialog
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen.AddGradeBottomSheet
import com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen.GradeEvent
import com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen.GradeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GradeScreen(
    navController: NavController,
    subjectId: Int,
    viewModel: GradeViewModel = hiltViewModel()
) {
    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()
    val scope = rememberCoroutineScope()

    val bottomState = rememberBottomSheetScaffoldState()

    val closeSheet: () -> Unit = {
        scope.launch {
            bottomState.bottomSheetState.collapse()
            viewModel.clearAfterGradeEvent()
            viewModel.removeAllErrors()
        }
    }

    val openSheet: () -> Unit = {
        scope.launch {
            bottomState.bottomSheetState.expand()
        }
    }

    if (bottomState.bottomSheetState.isCollapsed && !viewModel.editState.value) {
        viewModel.removeAllErrors()
        viewModel.clearAfterGradeEvent()
    }

    LaunchedEffect(key1 = true) {
        viewModel.setSubjectId(subjectId)
        viewModel.onEvent(GradeEvent.LoadGrades)
        viewModel.onEvent(GradeEvent.GetSpecificSubject)
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {
                    bottomState.snackbarHostState.showSnackbar(
                        message = it.uiText
                    )
                }
            }
        }
    }

    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        scaffoldState = bottomState,
        sheetContent = {
            AddGradeBottomSheet(
                onCloseBottomSheet = closeSheet,
                fredoka = fredoka,
                scaffoldState = bottomState,

                )
        },
    ) {
        Scaffold {
            LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                item {
                    GradeTitle(
                        fredoka = fredoka,
                        viewModel = viewModel,
                        onOpenBottomSheet = { openSheet() },
                        bottomSheetState = bottomState
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                }
                items(viewModel.gradeListState.value.listData) {
                    GradeListItem(
                        fredoka = fredoka,
                        data = it,
                        viewModel = viewModel,
                        onOpenBottomSheet = openSheet
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 0.8.dp
                    )
                }
            }
            if (viewModel.deleteDialogState.value) {
                DeleteDialog(
                    onDismissRequest = { viewModel.setDeleteDialogState(false) },
                    onDeleteClicked = { viewModel.onEvent(GradeEvent.DeleteSpecificGrade) },
                    title = stringResource(id = R.string.sureToDelete),
                    text = stringResource(id = R.string.thisChangeCannotBeReset)
                )
            }
        }
    }
}

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.compose.material.ExperimentalMaterialApi::class
)
@Composable
fun GradeTitle(
    fredoka: FontFamily,
    viewModel: GradeViewModel,
    onOpenBottomSheet: () -> Unit,
    bottomSheetState: BottomSheetScaffoldState
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.onEvent(GradeEvent.NavBackToSubjectScreen) },
                modifier = Modifier.scale(1.2f)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
            Text(
                text = viewModel.subjectNameState.value,
                fontFamily = fredoka,
                fontWeight = FontWeight.Medium,
                fontSize = 32.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(start = 5.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!viewModel.deleteState.value && !viewModel.editState.value) {
                IconButton(
                    onClick = { onOpenBottomSheet() },
                    enabled = bottomSheetState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )
                }

                IconButton(
                    onClick = { viewModel.setDropDownMenuState(!viewModel.dropDownMenuState.value) },
                    enabled = bottomSheetState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "",
                        modifier = Modifier.scale(1.2f)
                    )
                    DropdownMenu(
                        expanded = viewModel.dropDownMenuState.value,
                        onDismissRequest = { viewModel.setDropDownMenuState(false) },
                        modifier = Modifier.clip(
                            RoundedCornerShape(8.dp)
                        )
                    ) {
                        DropdownMenuItem(
                            onClick = { viewModel.setEditState(true) },
                            enabled = bottomSheetState.bottomSheetState.isCollapsed
                        ) {
                            Row {
                                Icon(Icons.Default.Edit, contentDescription = "")
                                Text(
                                    text = stringResource(id = R.string.edit),
                                    fontFamily = fredoka,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                        DropdownMenuItem(
                            onClick = { viewModel.setDeleteState(true) },
                            enabled = bottomSheetState.bottomSheetState.isCollapsed
                        ) {
                            Row {
                                Icon(Icons.Default.Delete, contentDescription = "")
                                Text(
                                    text = stringResource(id = R.string.delete),
                                    fontFamily = fredoka,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = {
                        viewModel.setDeleteState(false)
                        viewModel.setEditState(false)
                        viewModel.setDropDownMenuState(false)
                    },
                    border = BorderStroke(1.4.dp, color = LightBlue),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.padding(end = 5.dp),
                    enabled = bottomSheetState.bottomSheetState.isCollapsed
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GradeListItem(
    fredoka: FontFamily,
    data: Grade,
    viewModel: GradeViewModel,
    onOpenBottomSheet: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    color = Color(
                        red = data.gradeColor.red,
                        green = data.gradeColor.green,
                        blue = data.gradeColor.blue
                    )
                ),
            contentAlignment = Center
        ) {
            Text(
                text = data.grade.toString(),
                color = Color.White,
                modifier = Modifier.padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = data.description,
                fontFamily = fredoka,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterVertically)
            )
            if (!viewModel.editState.value && !viewModel.deleteState.value) {
                Text(
                    text = if (data.isWritten)
                        stringResource(id = R.string.classTest)
                    else stringResource(
                        id = R.string.oralGrade
                    ),
                    fontFamily = fredoka,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            } else {
                AnimatedVisibility(
                    visible = viewModel.deleteState.value,
                    enter = fadeIn(animationSpec = tween(10000))
                ) {
                    OutlinedButton(onClick = {
                        viewModel.setDeleteGradeIdState(data.id)
                        viewModel.setDeleteDialogState(true)
                    }, shape = CircleShape) {
                        Icon(Icons.Default.Clear, contentDescription = "")
                    }
                }

                AnimatedVisibility(
                    visible = !viewModel.deleteState.value,
                    enter = fadeIn(animationSpec = tween(1000))
                ) {
                    IconButton(onClick = {
                        viewModel.setSpecificGradeState(data)
                        viewModel.setEditTextFieldState(true)
                        viewModel.setEditState(true)
                        onOpenBottomSheet()
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "")
                    }
                }
            }
        }
    }
}