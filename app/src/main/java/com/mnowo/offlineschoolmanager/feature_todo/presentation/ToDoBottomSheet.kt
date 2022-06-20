package com.mnowo.offlineschoolmanager.feature_todo.presentation

import android.util.Log.d
import android.widget.CalendarView
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.SubjectPickerDialog
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.util.AddSubjectTestTags
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import com.mnowo.offlineschoolmanager.rememberFredoka
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoBottomSheet(
    onCloseBottomSheet: () -> Unit,
    viewModel: ToDoViewModel = hiltViewModel()
) {
    val fredoka = rememberFredoka()
    val bottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val closeSheet: () -> Unit = {
        scope.launch {
            bottomState.hide()
        }
    }

    val openSheet: () -> Unit = {
        scope.launch {
            bottomState.animateTo(targetValue = ModalBottomSheetValue.Expanded)
        }
    }

    if (viewModel.contentEditState.value) {
        viewModel.setContentEditState(value = false)

        viewModel.specificEditToDoState.value?.let { toDo ->
            viewModel.onEvent(ToDoEvent.EnteredTitle(toDo.title))
            viewModel.onEvent(ToDoEvent.EnteredDescription(toDo.description))
            viewModel.onEvent(ToDoEvent.EnteredDate(FormatDate.formatLongToDate(toDo.until)))
            val subject = viewModel.subjectList.value.listData.filter { it.id == toDo.subjectId }[0]
            viewModel.onEvent(ToDoEvent.ChangePickedSubject(subject = subject))
        }

    }

    if (!viewModel.bottomSheetState.value) {
        viewModel.onEvent(ToDoEvent.ChangeBottomSheetState(true))
        onCloseBottomSheet()
    }

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        sheetContent = {
            AddSubjectBottomSheet(
                onCloseBottomSheet = { closeSheet() },
                fredoka = fredoka
            )
        }
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(color = Color.LightGray)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(start = 20.dp, end = 20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                TextButton(
                    onClick = { onCloseBottomSheet() },
                    modifier = Modifier
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text(text = stringResource(id = R.string.back), color = LightBlue)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = {
                            if (!viewModel.editState.value) {
                                viewModel.onEvent(ToDoEvent.AddToDoEvent)
                            } else {
                                viewModel.onEvent(ToDoEvent.EditToDo)
                            }
                        },
                        border = BorderStroke(1.dp, color = LightBlue),
                        modifier = Modifier.testTag(AddSubjectTestTags.ADD_BUTTON)
                    ) {
                        Text(
                            text = if (!viewModel.editState.value) {
                                stringResource(id = R.string.add)
                            } else {
                                stringResource(id = R.string.save)
                            },
                            color = LightBlue
                        )
                    }
                }
            }
            Divider(modifier = Modifier.padding(top = 40.dp), color = Color.LightGray, 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = viewModel.titleState.value.text,
                    onValueChange = {
                        if (it.length <= 25) {
                            viewModel.onEvent(ToDoEvent.EnteredTitle(it))
                        }
                    },
                    label = {
                        Text(text = stringResource(R.string.enterTitle))
                    },
                    singleLine = true,
                    isError = viewModel.titleErrorState.value
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                OutlinedTextField(
                    value = viewModel.descriptionState.value.text,
                    onValueChange = {
                        viewModel.onEvent(ToDoEvent.EnteredDescription(it))
                    },
                    label = {
                        Text(text = stringResource(R.string.enterDescription))
                    },
                    maxLines = 6,
                    isError = viewModel.descriptionErrorState.value
                )
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                Text(
                    text = "Current date: ${FormatDate.formatDateToString(viewModel.datePickerDateState.value)}",
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(ToDoEvent.ChangeDatePickerState(isActive = true))
                    },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.pickDate), fontFamily = fredoka)
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Icon(Icons.Outlined.AccessTime, contentDescription = "")
                    }
                }
                DatePicker(viewModel = viewModel, fredoka = fredoka, onDateSelected = {
                    viewModel.onEvent(ToDoEvent.EnteredDate(it))
                })
                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                Text(
                    text = "${stringResource(id = R.string.currentSubject)} ${viewModel.pickedSubjectState.value.subjectName}",
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(ToDoEvent.ChangeSubjectPickerDialogState(isActive = true))
                    },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    border = BorderStroke(1.dp, viewModel.pickSubjectColorState.value)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.pickSubject), fontFamily = fredoka)
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Icon(Icons.Outlined.School, contentDescription = "")
                    }
                }
                if (viewModel.subjectPickerDialogState.value) {
                    SubjectPickerDialog(
                        onSubjectPicked = {
                            viewModel.onEvent(ToDoEvent.ChangePickedSubject(it))
                            viewModel.onEvent(
                                ToDoEvent.ChangeSubjectPickerDialogState(
                                    false
                                )
                            )
                        },
                        onDismissRequest = {
                            viewModel.onEvent(
                                ToDoEvent.ChangeSubjectPickerDialogState(
                                    false
                                )
                            )
                        },
                        fredoka = fredoka,
                        subjectsList = viewModel.subjectList.value.listData,
                        onAddNewSubjectClicked = {
                            openSheet()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DatePicker(viewModel: ToDoViewModel, fredoka: FontFamily, onDateSelected: (Date) -> Unit) {
    if (viewModel.datePickerState.value) {
        Dialog(onDismissRequest = { viewModel.onEvent(ToDoEvent.ChangeDatePickerState(false)) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f), shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.selectDate),
                            fontFamily = fredoka,
                            fontWeight = FontWeight.Medium
                        )
                        OutlinedButton(onClick = {
                            viewModel.onEvent(
                                ToDoEvent.ChangeDatePickerState(
                                    false
                                )
                            )
                        }) {
                            Text(text = stringResource(R.string.apply))
                        }
                    }
                    Text(
                        text = FormatDate.formatDateToString(viewModel.datePickerDateState.value),
                        fontFamily = fredoka,
                        modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
                    )
                    Divider()
                    AndroidView(
                        { CalendarView(it) },
                        modifier = Modifier.wrapContentWidth(),
                        update = { views ->
                            views.setOnDateChangeListener { _, year, month, dayOfMonth ->
                                onDateSelected(
                                    Calendar.getInstance().apply {
                                        set(year, month, dayOfMonth)
                                    }.time
                                )
                            }
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    }
                }
            }
        }
    }
}
