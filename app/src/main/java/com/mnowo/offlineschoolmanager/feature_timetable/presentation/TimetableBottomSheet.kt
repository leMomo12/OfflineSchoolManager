package com.mnowo.offlineschoolmanager.feature_timetable.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_grade.presentation.grade_screen.GradeEvent

@Composable
fun TimetableBottomSheet(
    viewModel: TimetableViewModel,
    fredoka: FontFamily,
    onCloseBottomSheet: () -> Unit
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
        ) {
            TextButton(onClick = { onCloseBottomSheet() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = LightBlue)
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                Text(text = stringResource(id = R.string.back), color = LightBlue)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = {

                    },
                    border = BorderStroke(1.dp, color = LightBlue)
                ) {
                    Text(
                        text = stringResource(id = R.string.add),
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

        }
    }
}