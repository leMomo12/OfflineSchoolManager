package com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen

import android.util.Log.d
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.Chip.Chip
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddGradeBottomSheet(
    viewModel: GradeViewModel = hiltViewModel(),
    onCloseBottomSheet: () -> Unit,
    fredoka: FontFamily,
    scaffoldState: BottomSheetScaffoldState,
) {

    val range by remember {
        if(viewModel.settingsState.value.isNormalGrade) {
            mutableStateOf(0.75f..6.5f)
        } else {
            mutableStateOf(0f..15f)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.uiText, duration = SnackbarDuration.Short
                    )
                }

                else -> {}
            }
        }
    }

    if (!viewModel.bottomSheetState.value) {
        viewModel.setBottomSheetState(true)
        onCloseBottomSheet()
    }

    // Setting TextField value to specificGradeState when editState is true
    if (viewModel.editTextFieldState.value) {
        viewModel.setEditTextFieldState(false)
        viewModel.onEvent(
            GradeEvent.EnteredClassTestDescription(
                description = viewModel.specificGradeState.value?.description ?: ""
            )
        )

        viewModel.onEvent(
            GradeEvent.EnteredGrade(
                grade = viewModel.specificGradeState.value?.grade?.toFloat() ?: 0.5f
            )
        )

        viewModel.onEvent(
            GradeEvent.EnteredIsWritten(
                isWritten = viewModel.specificGradeState.value?.isWritten ?: true
            )
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
        ) {
            TextButton(onClick = { onCloseBottomSheet() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                Text(text = stringResource(id = R.string.back), color = LightBlue)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = {
                        if (viewModel.editState.value) {
                            viewModel.onEvent(GradeEvent.UpdateGrade)
                        } else {
                            viewModel.onEvent(GradeEvent.AddGrade)
                        }
                    },
                    border = BorderStroke(1.dp, color = LightBlue)
                ) {
                    Text(
                        text =
                        if (viewModel.editState.value) {
                            stringResource(id = R.string.save)
                        } else {
                            stringResource(id = R.string.add)
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
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.classTestDescriptionErrorState.value,
                value = viewModel.classTestDescriptionState.value.text,
                label = { Text(text = stringResource(id = R.string.classTestDescription)) },
                onValueChange = {
                    viewModel.onEvent(GradeEvent.EnteredClassTestDescription(it))
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(top = 30.dp))
            Text(text = stringResource(id = R.string.grade) + ":", color = Color.Gray, fontFamily = fredoka)
            Text(text = "${viewModel.gradeState.value}", fontFamily = fredoka)
            Slider(
                value = viewModel.gradeState.value,
                onValueChange = { viewModel.onEvent(GradeEvent.EnteredGrade(grade = it)) },
                valueRange = range
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.written), fontFamily = fredoka)
                    RadioButton(selected = viewModel.isWrittenState.value, onClick = {
                        if (!viewModel.isWrittenState.value) {
                            viewModel.onEvent(GradeEvent.EnteredIsWritten(true))
                        }
                    })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.Orally),
                        fontFamily = fredoka,
                    )
                    RadioButton(
                        selected = !viewModel.isWrittenState.value,
                        onClick = {
                            if (viewModel.isWrittenState.value) {
                                viewModel.onEvent(GradeEvent.EnteredIsWritten(false))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GradeSliderChips(viewModel: GradeViewModel, fredoka: FontFamily) {
    LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        items(10) {
            Chip(text = "$it",
                borderColor = Color.Gray,
                fredoka = fredoka,
                onClick = { viewModel.onEvent(GradeEvent.EnteredGrade(1.5f)) }
            )
            Spacer(modifier = Modifier.padding(end = 10.dp))
        }

    }
}