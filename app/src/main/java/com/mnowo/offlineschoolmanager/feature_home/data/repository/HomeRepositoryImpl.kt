package com.mnowo.offlineschoolmanager.feature_home.data.repository

import com.mnowo.offlineschoolmanager.feature_home.data.local.HomeDao
import com.mnowo.offlineschoolmanager.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dao: HomeDao
) : HomeRepository {

    override suspend fun getAverage(): List<Double> {
        return dao.getAverage()
    }

    override suspend fun getCountOfSubjects(): Int {
        return dao.getCountOfSubjects()
    }

}