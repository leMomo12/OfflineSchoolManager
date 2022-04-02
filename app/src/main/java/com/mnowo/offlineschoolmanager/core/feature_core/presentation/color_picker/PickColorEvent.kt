package com.mnowo.offlineschoolmanager.core.feature_core.presentation.color_picker

import androidx.compose.ui.graphics.Color

sealed class PickColorEvent {
    object DismissDialog: PickColorEvent()
    data class ColorPicked(val color: Color): PickColorEvent()
}
