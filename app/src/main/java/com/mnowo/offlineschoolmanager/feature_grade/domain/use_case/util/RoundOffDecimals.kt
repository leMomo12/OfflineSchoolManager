package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import java.math.RoundingMode
import java.text.DecimalFormat

object RoundOffDecimals {

    fun roundOffDoubleDecimals(grade: Double) : Double {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(grade).toDouble()
    }
}