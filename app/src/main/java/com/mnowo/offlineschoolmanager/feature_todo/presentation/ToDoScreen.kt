package com.mnowo.offlineschoolmanager

import android.util.Log.d
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.core.feature_core.domain.models.UiEvent
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.AddSubjectBottomSheet
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoBottomSheet
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoScreen(navController: NavController, viewModel: ToDoViewModel = hiltViewModel()) {
    val fredoka = rememberFredoka()
    val bottomState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val staggeredText = listOf(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s when an unknown printer took a galley of type",
        "Lorem Ipsum is simply dummy text",
        "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here'",
        "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.",
        "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
    )


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
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }



    BottomSheetScaffold(
        scaffoldState = bottomState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        sheetContent = {
            ToDoBottomSheet(onCloseBottomSheet = { closeSheet() }, viewModel = viewModel)
        },
        sheetElevation = 5.dp
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(toDo = true, onClick = {
                    viewModel.bottomNav(it, currentScreen = Screen.ToDoScreen)
                })
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    ToDoTitle(fredoka = fredoka, openSheet = { openSheet() })
                }
                item {
                    ToDoStaggeredGrid(
                        listData = viewModel.toDoList.value.listData,
                        fredoka = fredoka,
                        viewModel = viewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 60.dp))
                }
            }
        }
    }
}

@Composable
fun ToDoTitle(fredoka: FontFamily, openSheet: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.toDoS),
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { openSheet() }) {
                Icon(Icons.Rounded.Add, contentDescription = "", modifier = Modifier.scale(1.2f))
            }
            IconButton(onClick = { }) {
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = "",
                    modifier = Modifier.scale(1.2f)
                )
            }
        }
    }
}

@Composable
fun ToDoStaggeredGrid(listData: List<ToDo>, fredoka: FontFamily, viewModel: ToDoViewModel) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        StaggeredVerticalGrid(
            numColumns = 2, //put the how many column you want
            modifier = Modifier.padding(5.dp)
        ) {
            listData.forEach { item ->
                val colorState = remember {
                    derivedStateOf {
                        if (viewModel.subjectList.value.listData.isNotEmpty()) {
                            val intColor =
                                viewModel.subjectList.value.listData.filter { it.id == item.subjectId }[0].color
                            return@derivedStateOf Color(intColor)
                        } else {
                            return@derivedStateOf Color.LightGray
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = colorState.value)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = {
                                    viewModel.onEvent(
                                        ToDoEvent.OnCheckboxChanged(
                                            toDoId = item.id,
                                            newValue = !item.isChecked
                                        )
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                            Text(
                                text = "Until: ${FormatDate.formatLongToSpring(item.until)}",
                                fontFamily = fredoka,
                                fontWeight = FontWeight.Light
                            )
                        }
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            fontFamily = fredoka,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = item.description,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            fontFamily = fredoka
                        )
                    }
                }
            }
        }
    }
}
