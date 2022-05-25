package com.mnowo.offlineschoolmanager.feature_grade.domain.use_case.util

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test

class RoundOffDecimalsTest {

    @Test
    fun roundDoubleUp() = runBlocking {
        val res = RoundOffDecimals.roundOffDoubleDecimals(3.788)
        Truth.assertThat(res).isEqualTo(3.79)
    }
}