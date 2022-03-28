package com.mnowo.offlineschoolmanager.core

import androidx.compose.ui.graphics.Color

sealed class PickColorEvent {
    object DismissDialog: PickColorEvent()
    data class ColorPicked(val color: Color): PickColorEvent()
}
