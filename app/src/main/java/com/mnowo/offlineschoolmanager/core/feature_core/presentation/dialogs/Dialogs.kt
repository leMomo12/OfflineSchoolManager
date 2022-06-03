package com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags

@Composable
fun DeleteDialog(onDismissRequest: () -> Unit, onDeleteClicked: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            Text(text = "Sure to delete?")
        },
        text = {
            Column() {
                Text(text = "This change cannot be reset")
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text(text = "No")
                }
                OutlinedButton(
                    onClick = { onDeleteClicked() },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(text = "Yes")
                }
            }
        }, shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun SubjectPickerDialog(
    onSubjectPicked: (Subject) -> Unit,
    onDismissRequest: () -> Unit,
    fredoka: FontFamily,
    subjectsList: List<Subject>
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(text = "Select Date", fontFamily = fredoka, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = "a",
                    color = Color.Transparent,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Divider()
                LazyColumn {
                    items(subjectsList) {
                        PickSubjectItem(
                            data = it,
                            fredoka = fredoka,
                            onSubjectPicked = { subject ->
                                onSubjectPicked(subject)
                            }
                        )
                    }
                    item {
                        // Add new Subject item
                    }
                }
            }
        }
    }
}

@Composable
fun PickSubjectItem(data: Subject, fredoka: FontFamily, onSubjectPicked: (Subject) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSubjectPicked(data)
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = Color(
                        red = data.color.red,
                        green = data.color.green,
                        blue = data.color.blue
                    )
                )
        )
        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
        Text(
            text = data.subjectName,
            fontFamily = fredoka,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun PickSubjectNewSubjectItem() {

}
