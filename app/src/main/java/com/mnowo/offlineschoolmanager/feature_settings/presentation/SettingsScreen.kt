package com.mnowo.offlineschoolmanager.feature_settings

import android.inputmethodservice.Keyboard
import android.widget.Switch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedTextField
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
import com.mnowo.offlineschoolmanager.feature_settings.presentation.SettingsEvent
import com.mnowo.offlineschoolmanager.feature_settings.presentation.SettingsViewModel
import com.mnowo.offlineschoolmanager.rememberFredoka
import org.w3c.dom.Text

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

    }
}

@Composable
fun SettingsGradySystem(viewModel: SettingsViewModel) {

}

@Composable
fun SettingsListItem(
    viewModel: SettingsViewModel,
    text: String,
    check: Boolean,
    onCheckedChange: () -> Unit
) {

}