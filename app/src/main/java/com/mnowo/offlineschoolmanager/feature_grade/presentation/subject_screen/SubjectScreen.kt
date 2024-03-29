package com.mnowo.offlineschoolmanager

import android.util.Log.d
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.rememberWindowInfo
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.DeleteDialog
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectViewModel
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen.SubjectEvent
import com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen.SubjectViewModel
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectScreen(
    navController: NavController,
    viewModel: SubjectViewModel = hiltViewModel(),
    addSubjectViewModel: AddSubjectViewModel = hiltViewModel()
) {

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
            addSubjectViewModel.clearAfterSubjectEvent()
            addSubjectViewModel.removeAllErrors()
            bottomState.bottomSheetState.expand()
        }
    }

    if (bottomState.bottomSheetState.isCollapsed && !viewModel.editState.value) {
        addSubjectViewModel.removeAllErrors()
        addSubjectViewModel.setEditState(false)
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
                fredoka = fredoka
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
                    SubjectTitle(
                        fredoka = fredoka, onOpenBottomSheet = {
                            openSheet()
                        },
                        viewModel = viewModel,
                        bottomSheetScaffoldState = bottomState
                    )
                }
                items(viewModel.subjectListState.value.listData) {
                    SubjectListItem(
                        fredoka = fredoka,
                        data = it,
                        onSubjectItemClicked = { index ->
                            viewModel.onEvent(SubjectEvent.OnSubjectClicked(index))
                        },
                        viewModel = viewModel,
                        onOpenBottomSheet = {
                            openSheet()
                        },
                        addSubjectViewModel = addSubjectViewModel
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 0.8.dp
                    )
                }
                item {
                    Spacer(Modifier.padding(vertical = 40.dp))
                }

            }
            if (viewModel.deleteDialogState.value) {
                DeleteDialog(
                    onDismissRequest = { viewModel.setDeleteDialogState(false) },
                    onDeleteClicked = {
                        viewModel.onEvent(SubjectEvent.DeleteSubject)
                    },
                    title = stringResource(id = R.string.sureToDelete),
                    text = stringResource(id = R.string.thisChangeCannotBeReset)
                )
            }
            if (viewModel.subjectListState.value.listData.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(id = R.drawable.education_icon), contentDescription = "")
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Text(
                        text = stringResource(R.string.emptySubjectListTitle),
                        fontFamily = fredoka,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource(R.string.emptySubjectListSubtitle),
                        fontFamily = fredoka,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.5f).padding(top = 5.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectTitle(
    fredoka: FontFamily,
    onOpenBottomSheet: () -> Unit,
    viewModel: SubjectViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.subjects),
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!viewModel.editState.value && !viewModel.deleteState.value) {
                IconButton(
                    onClick = {
                        onOpenBottomSheet()
                    },
                    modifier = Modifier.testTag(GradeTestTags.ADD_BUTTON),
                    enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "ADD_BUTTON",
                        modifier = Modifier
                            .scale(1.2f)
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.setDropDownMenuState(!viewModel.dropDownMenuState.value)
                    },
                    modifier = Modifier.testTag(GradeTestTags.MORE_BUTTON),
                    enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "",
                        modifier = Modifier
                            .scale(1.2f)
                    )
                    DropdownMenu(
                        expanded = viewModel.dropDownMenuState.value,
                        onDismissRequest = { viewModel.setDropDownMenuState(false) },
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .testTag(GradeTestTags.DROPDOWN_MENU),
                    ) {
                        DropdownMenuItem(
                            onClick = { viewModel.setEditState(true) },
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
                            onClick = { viewModel.setDeleteState(true) },
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
                OutlinedButton(
                    onClick = {
                        viewModel.setDeleteState(false)
                        viewModel.setEditState(false)
                        viewModel.setDropDownMenuState(false)
                    },
                    border = BorderStroke(1.4.dp, color = LightBlue),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .testTag(GradeTestTags.CANCEL_BUTTON)
                        .padding(end = 5.dp),
                    enabled = bottomSheetScaffoldState.bottomSheetState.isCollapsed,
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
fun SubjectListItem(
    fredoka: FontFamily,
    data: Subject,
    onSubjectItemClicked: (Int) -> Unit,
    viewModel: SubjectViewModel,
    onOpenBottomSheet: () -> Unit,
    addSubjectViewModel: AddSubjectViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!viewModel.deleteState.value && !viewModel.editState.value) {
                    onSubjectItemClicked(data.id)
                }
            }
            .padding(15.dp)
            .testTag(GradeTestTags.LIST_ROW)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            ),
        verticalAlignment = Alignment.CenterVertically
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
                        blue = data.color.blue,
                        alpha = data.color.alpha
                    )
                )
                .testTag(GradeTestTags.COLOR_BOX)
        )
        Spacer(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = data.subjectName,
                fontFamily = fredoka,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(CenterVertically)
                    .testTag(GradeTestTags.SUBJECT_TEXT)
            )
            if (!viewModel.deleteState.value && !viewModel.editState.value) {
                Text(
                    text = data.average.toString(),
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray,
                    modifier = Modifier.testTag(GradeTestTags.AVERAGE_TEXT)
                )
            } else {
                AnimatedVisibility(
                    visible = viewModel.deleteState.value,
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.setSubjectIdState(data.id)
                            viewModel.setDeleteDialogState(true)
                        },
                        shape = CircleShape,
                        modifier = Modifier.testTag(GradeTestTags.DELETE_BUTTON)
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "")
                    }
                }

                AnimatedVisibility(
                    visible = !viewModel.deleteState.value,
                ) {
                    IconButton(onClick = {
                        addSubjectViewModel.setSpecificSubjectState(data)
                        addSubjectViewModel.setEditState(true)
                        addSubjectViewModel.setEditTextFieldState(true)
                        onOpenBottomSheet()
                    }, modifier = Modifier.testTag(GradeTestTags.EDIT_BUTTON)) {
                        Icon(Icons.Default.Edit, contentDescription = "")
                    }
                }
            }
        }
    }
}
