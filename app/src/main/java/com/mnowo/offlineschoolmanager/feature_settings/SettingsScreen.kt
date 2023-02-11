package com.mnowo.offlineschoolmanager.feature_settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.rememberFredoka

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val fredoka = rememberFredoka()


    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.settings),
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fredoka,
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        SettingsListItem(viewModel = viewModel)
    }
}

@Composable
fun SettingsListItem(viewModel: SettingsViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Oberstufen Punkte-System")
        Switch(
            checked = viewModel.isNormalGradeFormatState.value,
            onCheckedChange = { viewModel.onEvent(SettingsEvent.SetIsNormalGradeFormatState) }
        )
    }
}