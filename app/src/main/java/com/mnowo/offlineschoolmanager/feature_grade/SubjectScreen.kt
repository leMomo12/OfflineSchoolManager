package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnowo.offlineschoolmanager.core.rememberWindowInfo

@Composable
fun SubjectScreen() {

    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()

    Scaffold(
        bottomBar = {
            //BottomAppBar(gradeAverage = true)
        }
    ) {
        LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
            item {
                SubjectTitle(fredoka = fredoka)
            }
            item { 
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
            }
            items(30) {
                SubjectListItem(fredoka = fredoka)
                Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray, thickness = 0.8.dp)
            }

        }
    }
}

@Composable
fun SubjectTitle(fredoka: FontFamily) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Subjects",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )

        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "", modifier = Modifier.scale(1.2f))
            }

            IconButton(onClick = {  }) {
                Icon(Icons.Default.MoreVert, contentDescription = "", modifier = Modifier.scale(1.2f))
            }
        }


    }
}

@Composable
fun SubjectListItem(fredoka: FontFamily) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Blue)
        )
        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Mathematics", fontFamily = fredoka, fontWeight = FontWeight.Light)
            Text(text = "3.2", fontFamily = fredoka, fontWeight = FontWeight.Light, color = Color.Gray)
        }
    }
}