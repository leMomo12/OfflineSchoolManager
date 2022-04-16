package com.mnowo.offlineschoolmanager.feature_grade.domain.models

sealed class AddGradeResult {
    object EmptyDescription : AddGradeResult()
    object GradeOutOffRange : AddGradeResult()
    object Exception : AddGradeResult()
    object Success : AddGradeResult()
}
