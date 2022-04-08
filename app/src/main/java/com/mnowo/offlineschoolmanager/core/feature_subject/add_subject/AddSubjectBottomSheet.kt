package com.mnowo.offlineschoolmanager.core


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Subject
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker.PickColorEvent
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.AddSubjectEvent
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.AddSubjectViewModel
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSubjectBottomSheet(
    viewModel: AddSubjectViewModel = hiltViewModel(),
    onCloseBottomSheet: () -> Unit,
    fredoka: FontFamily,
    scaffoldState: BottomSheetScaffoldState
) {

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {

            when (it) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.uiText, duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    if (!viewModel.bottomSheetState.value) {
        viewModel.setBottomSheetState(true)
        onCloseBottomSheet()
    }

    if (viewModel.showColorDialog.value) {
        PickColorDialog(
            fredoka = fredoka,
            onDismissClicked = {
                viewModel.onPickColorEvent(PickColorEvent.DismissDialog)
            },
            onColorPicked = {
                viewModel.onPickColorEvent(PickColorEvent.ColorPicked(color = it))
            }
        )
    }

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
                .clickable {
                    onCloseBottomSheet()
                }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            Text(text = "Back", color = LightBlue)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = { viewModel.onAddSubjectEvent(AddSubjectEvent.AddSubject) },
                    border = BorderStroke(1.dp, color = LightBlue)
                ) {
                    Text(text = "Add", color = LightBlue)
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
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.subjectErrorState.value,
                value = viewModel.subjectState.value.text,
                label = { Text(text = "Subject name") },
                onValueChange = {
                    viewModel.onAddSubjectEvent(AddSubjectEvent.EnteredSubject(it))
                },
                singleLine = true
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(color = viewModel.colorState.value)
                )
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                OutlinedButton(
                    onClick = { viewModel.onAddSubjectEvent(AddSubjectEvent.PickedColor) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Pick color")
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.roomErrorState.value,
                value = viewModel.roomState.value.text,
                label = { Text(text = "Room") },
                onValueChange = {
                    viewModel.onAddSubjectEvent(AddSubjectEvent.EnteredRoom(it))
                },
                singleLine = true
            )

            OutlinedTextField(
                label = { Text(text = "Written percentage") },
                value = viewModel.writtenPercentageState.value.text,
                onValueChange = {
                    viewModel.onAddSubjectEvent(AddSubjectEvent.EnteredWrittenPercentage(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                trailingIcon = {
                    Text(text = "%")
                },
                isError = viewModel.writtenErrorState.value
            )

            OutlinedTextField(
                label = { Text(text = "Oral percentage") },
                value = viewModel.oralPercentageState.value.text,
                onValueChange = {
                    viewModel.onAddSubjectEvent(AddSubjectEvent.EnteredOralPercentage(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                trailingIcon = {
                    Text(text = "%")
                },
                isError = viewModel.oralErrorState.value
            )
            Text(
                text = "Must exactly add up to 100",
                color = if (viewModel.mustAddUpTo100ErrorState.value) {
                    Color.Red
                } else {
                    Color.Gray
                },
                fontFamily = fredoka,
                fontWeight = FontWeight.Light
            )
        }
    }
}