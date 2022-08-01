package com.mnowo.offlineschoolmanager.feature_exam.presentation

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.DatePicker
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.SubjectPickerDialog
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExamBottomSheet(viewModel: ExamViewModel, onCloseBottomSheet: () -> Unit, fredoka: FontFamily) {

    val bottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val openSheet: () -> Unit = {
        coroutineScope.launch {
            bottomState.animateTo(targetValue = ModalBottomSheetValue.Expanded)
        }
    }

    val closeSheet: () -> Unit = {
        coroutineScope.launch {
            bottomState.hide()
        }
    }

    if (!viewModel.bottomSheetState.value) {
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
        }) {
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
                TextButton(onClick = { onCloseBottomSheet() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text(text = stringResource(id = R.string.back), color = LightBlue)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = {
                            viewModel.onEvent(ExamEvent.AddExamItem)
                        },
                        border = BorderStroke(1.dp, color = LightBlue)
                    ) {
                        Text(
                            text = stringResource(id = R.string.add),
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
                        viewModel.onEvent(ExamEvent.SetTitleState(text = it))
                    },
                    label = {
                        Text(stringResource(id = R.string.enterTitle))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.descriptionState.value.text,
                    onValueChange = {
                        viewModel.onEvent(ExamEvent.SetDescriptionState(text = it))
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(stringResource(id = R.string.enterDescription))
                    }
                )
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                Text(
                    text = stringResource(R.string.currentDate) + " " + FormatDate.formatDateToString(
                        viewModel.dateState.value
                    ),
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(ExamEvent.SetDatePickerState(value = true))
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

                DatePicker(
                    fredoka = fredoka,
                    onDateSelected = {
                        viewModel.onEvent(ExamEvent.SetDateState(it))
                    },
                    datePickerState = viewModel.datePickerState.value,
                    onDismissRequest = { viewModel.onEvent(ExamEvent.SetDatePickerState(false)) },
                    dateText = FormatDate.formatDateToString(viewModel.dateState.value)
                )

                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                Text(
                    text = "${stringResource(id = R.string.currentSubject)} ${viewModel.pickedSubjectState.value.subjectName}",
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(ExamEvent.SetSubjectPickerState(value = true))
                    },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    border = BorderStroke(1.dp, viewModel.pickedSubjectColorState.value)
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
                if (viewModel.subjectPickerState.value) {
                    SubjectPickerDialog(
                        onSubjectPicked = {
                            viewModel.onEvent(ExamEvent.SetPickedSubjectState(subject = it))
                            viewModel.onEvent(
                                ExamEvent.SetSubjectPickerState(value = false)
                            )
                        },
                        onDismissRequest = {
                            viewModel.onEvent(
                                ExamEvent.SetSubjectPickerState(value = false)
                            )
                        },
                        fredoka = fredoka,
                        subjectsList = viewModel.subjectListState.value.listData,
                        onAddNewSubjectClicked = {
                            openSheet()
                        }
                    )
                }
            }
        }
    }
}