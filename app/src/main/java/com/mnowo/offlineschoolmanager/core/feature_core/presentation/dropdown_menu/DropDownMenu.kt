package com.mnowo.offlineschoolmanager.core.feature_core.presentation.dropdown_menu

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent

@Composable
fun EditAndDeleteDropdownMenu(
    fredoka: FontFamily,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditMenuClicked: () -> Unit,
    editMenuEnabled: Boolean,
    onDeleteMenuClicked: () -> Unit,
    deleteMenuEnabled: Boolean
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            onDismissRequest()
        },
        modifier = Modifier
            .clip(
                RoundedCornerShape(8.dp)
            )
            .testTag(GradeTestTags.DROPDOWN_MENU)
    ) {
        DropdownMenuItem(
            onClick = { onEditMenuClicked() },
            modifier = Modifier.testTag(GradeTestTags.EDIT_MENU_ITEM),
            enabled = editMenuEnabled
        ) {
            Row {
                Icon(Icons.Default.Edit, contentDescription = "")
                Text(
                    text = stringResource(id = R.string.edit),
                    fontFamily = fredoka,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
            }
        }
        DropdownMenuItem(
            onClick = { onDeleteMenuClicked() },
            modifier = Modifier.testTag(GradeTestTags.DELETE_MENU_ITEM),
            enabled = deleteMenuEnabled
        ) {
            Row {
                Icon(Icons.Default.Delete, contentDescription = "")
                Text(
                    text = stringResource(id = R.string.delete),
                    fontFamily = fredoka,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
            }
        }
    }
}