package com.mnowo.offlineschoolmanager.feature_exam.presentation

import androidx.compose.ui.graphics.Color
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import java.util.*

sealed interface ExamEvent {
    data class SetTitleState(var text: String) : ExamEvent
    data class SetDescriptionState(var text: String) : ExamEvent
    data class SetDateState(var date: Date) : ExamEvent
    data class SetDatePickerState(var value: Boolean) : ExamEvent
    data class SetSubjectPickerState(var value: Boolean) : ExamEvent
    data class SetPickedSubjectColorState(var color: Color) : ExamEvent
    data class SetPickedSubjectState(var subject: Subject) : ExamEvent
    data class SetSubjectListState(var list: List<Subject>) : ExamEvent
    data class ChangeBottomSheetState(var value: Boolean) : ExamEvent
    data class SetExamListState(var list: List<Exam>) : ExamEvent

    object AddExamItem : ExamEvent
    object GetAllExamItems : ExamEvent
}
