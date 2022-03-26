package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.composesurveyapp.core.presentation.util.UiEvent
import com.mnowo.offlineschoolmanager.core.Screen
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@Composable
fun ToDoScreen(navController: NavController, viewModel: ToDoViewModel = hiltViewModel()) {
    val fredoka = rememberFredoka()

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

    Scaffold(
        bottomBar = {
            BottomAppBar(toDo = true, onClick = {
                viewModel.bottomNav(it, currentScreen = Screen.ToDoScreen)
            })
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ToDoTitle(fredoka = fredoka)
            }
            item {
                ToDoStaggeredGrid(staggeredText = staggeredText)
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 60.dp))
            }
        }
    }
}

@Composable
fun ToDoTitle(fredoka: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = " To Do's",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
        IconButton(onClick = {  }) {
            Icon(Icons.Rounded.Add, contentDescription = "", modifier = Modifier.scale(1.2f))
        }
    }
}

@Composable
fun ToDoStaggeredGrid(staggeredText: List<String>) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        StaggeredVerticalGrid(
            numColumns = 2, //put the how many column you want
            modifier = Modifier.padding(5.dp)
        ) {
            staggeredText.forEach { text ->
                val rnd = Random()
                val color: Int = android.graphics.Color.argb(
                    255,
                    rnd.nextInt(256),
                    rnd.nextInt(256),
                    rnd.nextInt(256)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    backgroundColor = Color(color = color),
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}