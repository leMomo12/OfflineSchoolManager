package com.mnowo.offlineschoolmanager.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Subject
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mnowo.offlineschoolmanager.core.theme.LightBlue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSubjectBottomSheet(
    onCloseBottomSheet: () -> Unit,
    subjectName: String,
    onSubjectNameChanged: (String) -> Unit,
    subjectError: Boolean,
    color: Color,
    onColorChangedClicked: () -> Unit,
    roomName: String,
    onRoomNameChanged: (String) -> Unit,
    roomError: Boolean,
    onAddClicked: () -> Unit
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
                .clickable {
                    onCloseBottomSheet()
                }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            Text(text = "Back", color = LightBlue)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = { onAddClicked() }, border = BorderStroke(1.dp, color = LightBlue)) {
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
                isError = subjectError,
                value = subjectName,
                label = { Text(text = "Subject name") },
                onValueChange = {
                    onSubjectNameChanged(it)
                })
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
                        .background(color = color)
                )
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                OutlinedButton(onClick = { onColorChangedClicked() }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Pick color")
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                isError = roomError,
                value = roomName,
                label = { Text(text = "Room") },
                onValueChange = {
                    onRoomNameChanged(it)
                }
            )
        }
    }
}