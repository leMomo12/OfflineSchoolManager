package com.mnowo.offlineschoolmanager.feature_grade.domain.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import kotlinx.coroutines.flow.Flow

interface GradeRepository {

    fun getAllSubjects() : Flow<List<Subject>>
}