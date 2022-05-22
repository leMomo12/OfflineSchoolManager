package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models

sealed class SubjectResult {
    object EmptySubjectText : SubjectResult()
    object DoesntAddUpTo100: SubjectResult()
    data class ErrorOccurred(val message: String?) : SubjectResult()
    object Success : SubjectResult()
}
