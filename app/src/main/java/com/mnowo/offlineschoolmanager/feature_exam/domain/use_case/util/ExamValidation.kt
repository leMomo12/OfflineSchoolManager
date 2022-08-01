package com.mnowo.offlineschoolmanager.feature_exam.domain.use_case.util

import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDoResult

object ExamValidation {

    fun validateExam(exam: Exam): ToDoResult {
        return if (exam.title.trim().isBlank()) {
            ToDoResult.EmptyTitle
        } else if (exam.description.trim().isBlank()) {
            ToDoResult.EmptyDescription
        }  else if (exam.subjectId == -1) {
            ToDoResult.SubjectNotPicked
        } else {
            ToDoResult.Success
        }
    }
}