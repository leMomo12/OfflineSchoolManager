package com.mnowo.offlineschoolmanager.core

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mnowo.offlineschoolmanager.core.theme.colorPickerList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PickColorDialog(
    fredoka: FontFamily,
    onDismissClicked: () -> Unit,
    onColorPicked: (Color) -> Unit,
    pickColorTitle: String
) {
    val colorList = colorPickerList

    Dialog(onDismissRequest = { onDismissClicked() }) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pickColorTitle,
                        fontFamily = fredoka,
                        fontWeight = FontWeight.Medium,
                        fontSize = 25.sp
                    )
                    IconButton(onClick = { onDismissClicked() }) {
                        Icon(Icons.Outlined.Cancel, contentDescription = "")
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                LazyVerticalGrid(
                    cells = GridCells.Adaptive(50.dp)
                ) {
                    items(colorList) { color ->
                        ColorPickItem(
                            color = color,
                            onColorPicked = {
                                onColorPicked(it)
                                onDismissClicked()
                            })
                    }
                }

            }
        }
    }
}

@Composable
fun ColorPickItem(color: Color, onColorPicked: (Color) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .size(60.dp)
            .padding(8.dp)
            .background(color = color)
            .clickable {
                onColorPicked(color)
            }
    )
}