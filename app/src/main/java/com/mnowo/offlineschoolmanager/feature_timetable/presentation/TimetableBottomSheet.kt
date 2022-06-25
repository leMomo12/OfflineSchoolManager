package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs.SubjectPickerDialog
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import java.sql.Time

@Composable
fun TimetableBottomSheet(
    viewModel: TimetableViewModel,
    fredoka: FontFamily,
    onCloseBottomSheet: () -> Unit
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
            Text(text = "Pick day:", fontFamily = fredoka, fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimetableOutlinedDayButton(
                    text = "M",
                    borderColor = Color.LightGray,
                    fredoka = fredoka
                )
                TimetableOutlinedDayButton(
                    text = "T",
                    borderColor = Color.LightGray,
                    fredoka = fredoka
                )
                TimetableOutlinedDayButton(
                    text = "W",
                    borderColor = Color.LightGray,
                    fredoka = fredoka
                )
                TimetableOutlinedDayButton(
                    text = "TH",
                    borderColor = Color.LightGray,
                    fredoka = fredoka
                )
                TimetableOutlinedDayButton(
                    text = "F",
                    borderColor = Color.LightGray,
                    fredoka = fredoka
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Text(text = stringResource(id = R.string.currentSubject) + " ${viewModel.pickedSubjectState.value?.subjectName ?: ""}", fontFamily = fredoka, fontWeight = FontWeight.Medium)
            OutlinedButton(
                onClick = {
                    viewModel.onEvent(TimetableEvent.OnSubjectDialogStateChanged(value = true))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(top = 5.dp),
                border = BorderStroke(1.dp, Color.LightGray)
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
                    onAddNewSubjectClicked = {}
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 20.dp))

            Text(text = "Pick hour:", fontFamily = fredoka, fontWeight = FontWeight.Medium)

            NumberPicker(
                value = viewModel.hourPickerState.value,
                onValueChange = { viewModel.onEvent(TimetableEvent.OnHourPickerChanged(it)) },
                range = 1..13
            )
        }
    }
}

@Composable
fun TimetableOutlinedDayButton(text: String, borderColor: Color, fredoka: FontFamily) {
    OutlinedButton(
        onClick = { },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(40.dp),
        border = BorderStroke(1.dp, color = borderColor)
    ) {
        Text(text = text, fontFamily = fredoka, fontWeight = FontWeight.Light)
    }
}