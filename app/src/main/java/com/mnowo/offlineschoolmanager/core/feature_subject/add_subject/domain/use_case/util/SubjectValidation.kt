package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.use_case.util

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.SubjectResult

object SubjectValidation {

    fun validateSubject(subject: Subject): SubjectResult {
        try {
            if(subject.subjectName.trim().isBlank()) {
                return SubjectResult.EmptySubjectText
            }

            if(subject.oralPercentage + subject.writtenPercentage != 100.0) {
                return SubjectResult.DoesntAddUpTo100
            }
            return SubjectResult.Success
        } catch (e: Exception) {
            return SubjectResult.ErrorOccurred(message = e.localizedMessage)
        }
    }
}