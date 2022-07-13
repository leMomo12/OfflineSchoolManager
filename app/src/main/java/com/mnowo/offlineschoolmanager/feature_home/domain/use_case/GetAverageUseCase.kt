package com.mnowo.offlineschoolmanager.feature_home.domain.use_case

import android.util.Log.d
import com.mnowo.offlineschoolmanager.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetAverageUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    suspend operator fun invoke(): List<Double> = repository.getAverage()
}