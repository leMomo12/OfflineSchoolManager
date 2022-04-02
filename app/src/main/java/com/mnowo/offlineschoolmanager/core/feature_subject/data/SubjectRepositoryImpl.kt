package com.mnowo.offlineschoolmanager.core.feature_subject.data

import com.mnowo.offlineschoolmanager.core.feature_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val dao: SubjectDao
) : SubjectRepository {

    override suspend fun addSubject(subject: Subject) {
        dao.addSubject(subject = subject)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return dao.getAllSubjects()
    }

    override suspend fun deleteSubject(id: Int) {
        return dao.deleteSubject(id = id)
    }

}