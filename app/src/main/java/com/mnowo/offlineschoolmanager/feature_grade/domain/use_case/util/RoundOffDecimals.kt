package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import android.util.Log.d
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

object RoundOffDecimals {

    fun roundOffDoubleDecimals(grade: Double) : Double {
        return try {
            return grade.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun roundOffDoubleToFourDecimals(grade: Double) : Double {
        return try {
            return grade.toBigDecimal().setScale(4, RoundingMode.UP).toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}