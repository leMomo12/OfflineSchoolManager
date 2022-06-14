package com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util

import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import java.text.SimpleDateFormat
import java.util.*

object FormatDate {

    fun formatDateToString(date: Date): String {
        return SimpleDateFormat(Constants.TODO_DATE_TO_STRING_FORMAT, Locale.US).format(date)
    }

    // Other date format
    fun formatLongToSpring(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(Constants.TODO_LONG_TO_STRING_FORMAT, Locale.US)
        return format.format(date)
    }

    fun formatLongToDate(time: Long): Date {
        return Date(time)
    }
 }