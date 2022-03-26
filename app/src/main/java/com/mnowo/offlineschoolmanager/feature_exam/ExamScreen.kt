package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExamScreen() {

    val fredoka = rememberFredoka()

    Scaffold(
        bottomBar = {
            //BottomAppBar(exam = true)
        }) {
        LazyColumn() {
            item { 
                ExamTitle(fredoka = fredoka)
            }
        }
    }
}

@Composable
fun ExamTitle(fredoka: FontFamily) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)) {
        Text(
            text = "Exam",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )
    }
}
