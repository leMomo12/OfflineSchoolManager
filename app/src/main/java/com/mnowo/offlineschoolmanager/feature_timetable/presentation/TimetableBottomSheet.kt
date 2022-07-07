package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import android.util.Log.d
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.SubjectPickerDialog
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import kotlinx.coroutines.launch
import java.sql.Time

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableBottomSheet(
    viewModel: TimetableViewModel,
    fredoka: FontFamily,
    onCloseBottomSheet: () -> Unit
) {

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

    if (!viewModel.timetableBottomSheetState.value) {
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
                TextButton(onClick = { onCloseBottomSheet() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text(text = stringResource(id = R.string.back), color = LightBlue)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = {
                            viewModel.onEvent(TimetableEvent.AddTimetable)
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
                AnimatedVisibility(visible = viewModel.alreadyTakenErrorState.value == Color.Red) {
                    Text(
                        text = "Day and hour already taken. Pick a different hour or day",
                        color = viewModel.alreadyTakenErrorState.value ,
                        fontFamily = fredoka,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }

                Text(text = "Pick day:", fontFamily = fredoka, fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    TimetableOutlinedDayButton(
                        text = "M",
                        borderColor = viewModel.pickedDayColorState.getValue(0),
                        fredoka = fredoka,
                        onClick = { viewModel.onEvent(TimetableEvent.OnPickedDayColorStateChanged(day = 0)) }
                    )

                    TimetableOutlinedDayButton(
                        text = "T",
                        borderColor = viewModel.pickedDayColorState.getValue(1),
                        fredoka = fredoka,
                        onClick = { viewModel.onEvent(TimetableEvent.OnPickedDayColorStateChanged(day = 1)) }
                    )
                    TimetableOutlinedDayButton(
                        text = "W",
                        borderColor = viewModel.pickedDayColorState.getValue(2),
                        fredoka = fredoka,
                        onClick = { viewModel.onEvent(TimetableEvent.OnPickedDayColorStateChanged(day = 2)) }
                    )
                    TimetableOutlinedDayButton(
                        text = "TH",
                        borderColor = viewModel.pickedDayColorState.getValue(3),
                        fredoka = fredoka,
                        onClick = { viewModel.onEvent(TimetableEvent.OnPickedDayColorStateChanged(day = 3)) }
                    )
                    TimetableOutlinedDayButton(
                        text = "F",
                        borderColor = viewModel.pickedDayColorState.getValue(4),
                        fredoka = fredoka,
                        onClick = { viewModel.onEvent(TimetableEvent.OnPickedDayColorStateChanged(day = 4)) }
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                Text(
                    text = stringResource(id = R.string.currentSubject) + " ${viewModel.pickedSubjectState.value?.subjectName ?: ""}",
                    fontFamily = fredoka,
                    fontWeight = FontWeight.Medium
                )
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(TimetableEvent.OnSubjectDialogStateChanged(value = true))
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(top = 5.dp),
                    border = BorderStroke(1.dp, viewModel.pickSubjectErrorState.value),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(id = R.string.pickSubject), fontFamily = fredoka)
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Icon(Icons.Outlined.School, contentDescription = "")
                    }
                }
                if (viewModel.subjectDialogState.value) {
                    SubjectPickerDialog(
                        onSubjectPicked = {
                            viewModel.onEvent(TimetableEvent.OnSubjectPicked(subject = it))
                            viewModel.onEvent(TimetableEvent.OnSubjectDialogStateChanged(value = false))
                        },
                        onDismissRequest = {
                            viewModel.onEvent(TimetableEvent.OnSubjectDialogStateChanged(value = false))
                        },
                        fredoka = fredoka,
                        subjectsList = viewModel.subjectListState.value.listData,
                        onAddNewSubjectClicked = { openSheet() }
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                Text(text = "Pick hour:", fontFamily = fredoka, fontWeight = FontWeight.Medium)

                NumberPicker(
                    value = viewModel.hourPickerState.value,
                    onValueChange = { viewModel.onEvent(TimetableEvent.OnHourPickerChanged(it)) },
                    range = 1..12
                )
            }
        }

    }

}

@Composable
fun TimetableOutlinedDayButton(
    text: String,
    borderColor: Color,
    fredoka: FontFamily,
    onClick: () -> Unit
) {
    d("BorderColor", "Color: $borderColor")
    OutlinedButton(
        onClick = { onClick() },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .size(40.dp),
        border = BorderStroke(1.dp, color = borderColor)
    ) {
        Text(text = text, fontFamily = fredoka, fontWeight = FontWeight.Light)
    }
}