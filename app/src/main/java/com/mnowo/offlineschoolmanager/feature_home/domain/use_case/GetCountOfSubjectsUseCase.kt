package com.mnowo.offlineschoolmanager.feature_home.domain.use_case

import com.mnowo.offlineschoolmanager.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetCountOfSubjectsUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    suspend operator fun invoke(): Int = repository.getCountOfSubjects()
}