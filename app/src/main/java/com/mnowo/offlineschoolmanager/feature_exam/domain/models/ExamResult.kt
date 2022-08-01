package com.mnowo.offlineschoolmanager.feature_exam.domain.models

sealed interface ExamResult {
    object EmptyTitle : ExamResult
    object EmptyDescription : ExamResult
    object SubjectNotPicked : ExamResult
    object Success : ExamResult
}
