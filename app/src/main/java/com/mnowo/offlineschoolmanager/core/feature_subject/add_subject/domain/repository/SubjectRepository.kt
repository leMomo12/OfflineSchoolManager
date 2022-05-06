package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.repository

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {

    suspend fun addSubject(subject: Subject)

    fun getAllSubjects() : Flow<List<Subject>>

    suspend fun deleteSubject(id: Int)
}