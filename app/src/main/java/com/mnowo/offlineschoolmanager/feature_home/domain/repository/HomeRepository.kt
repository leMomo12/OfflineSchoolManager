package com.mnowo.offlineschoolmanager.feature_home.domain.repository

interface HomeRepository {

    suspend fun getAverage() : List<Double>

    suspend fun getCountOfSubjects() : Int
}