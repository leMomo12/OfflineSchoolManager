package com.mnowo.offlineschoolmanager.core.feature_core.domain.models

data class TextFieldState(
    var text: String = ""
) {
    fun clearText() {
        text = ""
    }
}
