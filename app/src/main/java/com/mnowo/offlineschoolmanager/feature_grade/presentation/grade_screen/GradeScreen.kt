package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.rememberWindowInfo
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
        }
    }

    val openSheet: () -> Unit = {
        scope.launch {
            bottomState.bottomSheetState.expand()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.setSubjectId(subjectId)
        viewModel.onEvent(GradeEvent.LoadGrades)
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
                scaffoldState = bottomState
            )
        },
    ) {
        Scaffold {
            LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                item {
                    GradeTitle(
                        fredoka = fredoka,
                        viewModel = viewModel,
                        onOpenBottomSheet = { openSheet() })
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                }
                items(viewModel.gradeListState.value.listData) {
                    GradeListItem(fredoka = fredoka, data = it)
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 0.8.dp
                    )
                }

            }
        }
    }
}

@Composable
fun GradeTitle(fredoka: FontFamily, viewModel: GradeViewModel, onOpenBottomSheet: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Mathematics",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onOpenBottomSheet() }) {
                Icon(Icons.Rounded.Add, contentDescription = "", modifier = Modifier.scale(1.2f))
            }

            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "",
                    modifier = Modifier.scale(1.2f)
                )
            }
        }


    }
}

@Composable
fun GradeListItem(fredoka: FontFamily, data: Grade) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), verticalAlignment = CenterVertically
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
                modifier = Modifier.fillMaxWidth(0.6f)
            )
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
        }
    }
}