package com.mnowo.offlineschoolmanager.feature_exam.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Constants
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExam(exam: Exam)

    @Query("SELECT * FROM ${Constants.EXAM_TABLE} ORDER BY date ASC")
    fun getAllExamItems() : Flow<List<Exam>>

}