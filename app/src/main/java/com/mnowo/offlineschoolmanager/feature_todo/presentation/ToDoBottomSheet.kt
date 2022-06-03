package com.mnowo.offlineschoolmanager.feature_todo.presentation

import android.text.format.DateFormat.format
import android.widget.CalendarView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.SubjectPickerDialog
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.util.AddSubjectTestTags
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.rememberFredoka
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ToDoBottomSheet(onCloseBottomSheet: () -> Unit, viewModel: ToDoViewModel = hiltViewModel()) {
    val fredoka = rememberFredoka()

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
                modifier = Modifier.testTag(AddSubjectTestTags.BACK_BUTTON)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                Text(text = stringResource(id = R.string.back), color = LightBlue)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = {
                    },
                    border = BorderStroke(1.dp, color = LightBlue),
                    modifier = Modifier.testTag(AddSubjectTestTags.ADD_BUTTON)
                ) {
                    Text(
                        text = if (true) {
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
                value = viewModel.descriptionState.value.text,
                onValueChange = {
                    viewModel.onEvent(ToDoEvent.EnteredDescription(it))
                },
                label = {
                    Text(text = "Enter description")
                }
            )
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Text(
                text = "Current date: ${viewModel.formatDateToString()}",
                fontFamily = fredoka,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            OutlinedButton(onClick = {
                viewModel.onEvent(ToDoEvent.ChangeDatePickerState(isActive = true))
            }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Pick date", fontFamily = fredoka)
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    Icon(Icons.Outlined.AccessTime, contentDescription = "")
                }
            }
            DatePicker(viewModel = viewModel, fredoka = fredoka, onDateSelected = {
                viewModel.onEvent(ToDoEvent.EnteredDate(it))
            })

            Spacer(modifier = Modifier.padding(vertical = 20.dp))



            Text(
                text = "Current subject: ${viewModel.formatDateToString()}",
                fontFamily = fredoka,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            OutlinedButton(onClick = {
                viewModel.onEvent(ToDoEvent.ChangeSubjectPickerDialogState(isActive = true))
            }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Pick subject", fontFamily = fredoka)
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    Icon(Icons.Outlined.AccessTime, contentDescription = "")
                }
            }
            if (viewModel.subjectPickerDialogState.value) {
                SubjectPickerDialog(
                    onSubjectPicked = {
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
                    subjectsList = listOf(
                        Subject(1, "German", -2423423, "234", 50.0, 50.0, 2.45),
                        Subject(2, "German", -2423423, "234", 50.0, 50.0, 2.45),
                        Subject(3, "German", -2423423, "234", 50.0, 50.0, 2.45)
                    )
                )
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
                    Text(text = "Select Date", fontFamily = fredoka, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(
                        text = viewModel.formatDateToString(),
                        fontFamily = fredoka,
                        modifier = Modifier.padding(bottom = 5.dp)
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
                        Button(onClick = { viewModel.onEvent(ToDoEvent.ChangeDatePickerState(false)) }) {
                            Text(text = "Apply")
                        }
                    }
                }
            }
        }
    }
}
