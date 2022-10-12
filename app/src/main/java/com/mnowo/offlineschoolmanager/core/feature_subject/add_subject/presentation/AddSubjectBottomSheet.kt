package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation


import android.net.ipsec.ike.ChildSaProposal
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.PickColorDialog
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker.PickColorEvent
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.util.AddSubjectTestTags
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSubjectBottomSheet(
    viewModel: AddSubjectViewModel = hiltViewModel(),
    onCloseBottomSheet: () -> Unit,
    fredoka: FontFamily
) {

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.ShowSnackbar -> {

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
            },
            pickColorTitle = stringResource(id = R.string.pickColor)
        )
    }

    if (viewModel.editTextFieldState.value) {
        viewModel.setEditTextFieldState(false)

        viewModel.onAddSubjectEvent(
            AddSubjectEvent.EnteredSubject(
                viewModel.specificSubjectState.value?.subjectName ?: ""
            )
        )
        viewModel.onAddSubjectEvent(
            AddSubjectEvent.EnteredRoom(
                viewModel.specificSubjectState.value?.room ?: ""
            )
        )
        viewModel.roundOffPercentage(
            ((viewModel.specificSubjectState.value?.writtenPercentage)?.div(100))?.toFloat() ?: 0.5f
        )

        viewModel.specificSubjectState.value?.color?.let {
            val color = Color(
                it.red,
                it.green,
                it.blue
            )
            viewModel.onPickColorEvent(
                PickColorEvent.ColorPicked(
                    color = color
                )
            )
        }
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
                        if (viewModel.editState.value) {
                            viewModel.onAddSubjectEvent(AddSubjectEvent.EditSubject)
                        } else {
                            viewModel.onAddSubjectEvent(AddSubjectEvent.AddSubject)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AddSubjectTestTags.SUBJECT_TEXT_FIELD),
                isError = viewModel.subjectErrorState.value,
                value = viewModel.subjectState.value.text,
                label = { Text(text = stringResource(id = R.string.subjectName)) },
                onValueChange = {
                    if (it.length <= 15)
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
                        .testTag(AddSubjectTestTags.COLOR_BOX)
                )
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                OutlinedButton(
                    onClick = { viewModel.onAddSubjectEvent(AddSubjectEvent.PickedColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(AddSubjectTestTags.PICK_COLOR_BUTTON)
                ) {
                    Text(text = stringResource(id = R.string.pickColor))
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AddSubjectTestTags.ROOM_TEXT_FIELD),
                isError = viewModel.roomErrorState.value,
                value = viewModel.roomState.value.text,
                label = { Text(text = stringResource(id = R.string.room)) },
                onValueChange = {
                    if (viewModel.roomState.value.text.length <= 10)
                        viewModel.onAddSubjectEvent(AddSubjectEvent.EnteredRoom(it))
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AddSubjectBottomSheetWrittenAndOral(
                    descriptionText = stringResource(id = R.string.written) + ":",
                    percentageText = "${viewModel.writtenPercentageState.value.toString()} %",
                    fredoka = fredoka,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(0.5f)
                )
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                )
                AddSubjectBottomSheetWrittenAndOral(
                    descriptionText = stringResource(R.string.oral) + ":",
                    percentageText = "${viewModel.oralPercentageState.value.toString()} %",
                    fredoka = fredoka,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(0.5f)
                )
            }
            Slider(
                value = viewModel.sliderState.value,
                onValueChange = {
                    viewModel.roundOffPercentage(writtenPercentage = it)
                },
            )
            AddSubjectBottomSheetSliderChips(fredoka = fredoka, viewModel = viewModel)
        }
    }
}


@Composable
fun RowScope.AddSubjectBottomSheetWrittenAndOral(
    descriptionText: String,
    percentageText: String,
    fredoka: FontFamily,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = descriptionText, fontFamily = fredoka, color = Color.Gray)
        Text(
            text = percentageText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = fredoka
        )
    }
}

@Composable
fun AddSubjectBottomSheetSliderChips(fredoka: FontFamily, viewModel: AddSubjectViewModel) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Chip(
            text = "30:70",
            borderColor = Color.Gray,
            fredoka = fredoka,
            onClick = { viewModel.roundOffPercentage(0.30f) }
        )
        Chip(
            text = "50:50",
            borderColor = Color.Gray,
            fredoka = fredoka,
            onClick = { viewModel.roundOffPercentage(0.50f) }
        )
        Chip(
            text = "70:30",
            borderColor = Color.Gray,
            fredoka = fredoka,
            onClick = { viewModel.roundOffPercentage(0.70f) }
        )
    }
}

@Composable
fun Chip(text: String, borderColor: Color, fredoka: FontFamily, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(32.dp))
            .border(1.dp, color = borderColor, shape = RoundedCornerShape(32.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fredoka,
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
        )
    }
}