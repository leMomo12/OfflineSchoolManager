package com.mnowo.offlineschoolmanager.feature_grade.data.repository


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_grade.data.local.GradeDao
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GradeRepositoryImpl @Inject constructor(
    private val gradeDao: GradeDao,
    private val dataStorePreferences: DataStore<Preferences>
) : GradeRepository {

    override fun addGrade(grade: Grade) {
        return gradeDao.addGrade(grade = grade)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return gradeDao.getAllSubjects()
    }

    override fun getAllGrades(subjectId: Int): Flow<List<Grade>> {
        return gradeDao.getAllGrades(subjectId = subjectId)
    }

    override fun updateAverage(newAverage: Double, subjectId: Int) {
        return gradeDao.updateAverage(newAverage = newAverage, subjectId = subjectId)
    }

    override fun getSpecificSubject(subjectId: Int): Subject {
        return gradeDao.getSpecificSubject(subjectId = subjectId)
    }

    override fun sumOfWrittenGrade(subjectId: Int): Double {
        return gradeDao.sumOfWrittenGrade(subjectId = subjectId)
    }

    override fun countOfWrittenGrade(subjectId: Int): Int {
        return gradeDao.countOfWrittenGrade(subjectId = subjectId)
    }

    override fun sumOfOralGrade(subjectId: Int): Double {
        return gradeDao.sumOfOralGrade(subjectId = subjectId)
    }

    override fun countOfOralGrade(subjectId: Int): Int {
        return gradeDao.countOfOralGrade(subjectId = subjectId)
    }

    override suspend fun deleteSpecificGrade(gradeId: Int): Int {
        return gradeDao.deleteSpecificGrade(gradeId = gradeId)
    }

    override suspend fun updateGrade(grade: Grade) {
        return gradeDao.updateGrade(grade = grade)
    }

    override suspend fun deleteSubject(subjectId: Int) {
        return gradeDao.deleteSubject(subjectId = subjectId)
    }

    override suspend fun deleteAllSubjectSpecificGrades(subjectId: Int) {
        return gradeDao.deleteAllSubjectSpecificGrades(subjectId = subjectId)
    }

    override suspend fun deleteAllSubjectsFromTimetable(subjectId: Int) {
        return gradeDao.deleteAllSubjectsFromTimetable(subjectId = subjectId)
    }

    override suspend fun deleteAllSubjectsFromToDo(subjectId: Int) {
        return gradeDao.deleteAllSubjectsFromToDo(subjectId = subjectId)
    }

    override suspend fun deleteAllSubjectsFromExam(subjectId: Int) {
        return gradeDao.deleteAllSubjectsFromExam(subjectId = subjectId)
    }

    override suspend fun getInAppCounter(): Int {
        val dataStoreKey = stringPreferencesKey(Constants.USER_IN_APP_COUNTER)
        val preferences = dataStorePreferences.data.first()
        return preferences[dataStoreKey]?.toInt() ?: 0
    }

}