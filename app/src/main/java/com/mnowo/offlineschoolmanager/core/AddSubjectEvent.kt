package com.mnowo.offlineschoolmanager.core

import androidx.compose.ui.graphics.Color

sealed class AddSubjectEvent {
    data class EnteredSubject(val subject: String) : AddSubjectEvent()
    data class EnteredRoom(val room: String) : AddSubjectEvent()
    data class PickedColor(val color: Color) : AddSubjectEvent()
}
