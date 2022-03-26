package com.mnowo.offlineschoolmanager.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
    subjectError: Boolean
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
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {
                    onCloseBottomSheet()
                }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            Text(text = "Back", color = LightBlue)
        }

        Divider(modifier = Modifier.padding(top = 40.dp), color = Color.LightGray, 1.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp, top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Subject name")

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    BasicTextField(
                        value = subjectName,
                        onValueChange = onSubjectNameChanged,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        maxLines = 1
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Pick Color")

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    Box(modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(10.dp)) {
                        Text(text = "")
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Room")

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    BasicTextField(
                        value = subjectName,
                        onValueChange = onSubjectNameChanged,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        maxLines = 1
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}