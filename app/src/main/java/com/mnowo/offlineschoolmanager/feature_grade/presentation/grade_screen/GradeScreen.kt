package com.mnowo.offlineschoolmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnowo.offlineschoolmanager.core.rememberWindowInfo

@Composable
fun GradeScreen() {
    val fredoka = rememberFredoka()
    val windowInfo = rememberWindowInfo()

    Scaffold() {
        LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
            item {
                GradeTitle(fredoka = fredoka)
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
            }
            items(30) {
                GradeListItem(fredoka = fredoka)
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.LightGray,
                    thickness = 0.8.dp
                )
            }

        }
    }
}

@Composable
fun GradeTitle(fredoka: FontFamily) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Mathematics",
            fontFamily = fredoka,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { }) {
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
fun GradeListItem(fredoka: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color = Color.Red),
            contentAlignment = Center
        ) {
            Text(text = "2.25", color = Color.White, modifier = Modifier.padding(2.dp))
        }


        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Mathematics", fontFamily = fredoka, fontWeight = FontWeight.Light)
            Text(
                text = "Class test",
                fontFamily = fredoka,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
    }
}