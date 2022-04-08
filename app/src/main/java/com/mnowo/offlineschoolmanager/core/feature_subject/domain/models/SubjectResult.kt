package com.mnowo.offlineschoolmanager.core.feature_subject.domain.models

sealed class SubjectResult {
    object EmptySubjectText : SubjectResult()
    object DoesntAddUpTo100: SubjectResult()
    object ErrorOccurred : SubjectResult()
    object Success : SubjectResult()
}
