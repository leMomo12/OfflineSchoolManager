package com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
