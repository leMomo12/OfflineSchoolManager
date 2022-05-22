package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import java.math.RoundingMode
import java.text.DecimalFormat

object RoundOffDecimals {

    fun roundOffDoubleDecimals(grade: Double) : Double {
        return try {
            val decimalFormat = DecimalFormat("#.##")
            decimalFormat.roundingMode = RoundingMode.CEILING
            decimalFormat.format(grade).toDouble()
        } catch (e: NumberFormatException) {
            -1.0
        }
    }
}