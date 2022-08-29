package com.mnowo.offlineschoolmanager.core.feature_core.domain.util

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_exam.domain.models.Exam
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import java.util.*

object ExamSubjectItemHelper {

    fun getSubjectItem(examData: Exam, subjectList: List<Subject>): Subject {
        val errorSubject = Subject(-1, "", 0, "fds", 50.0, 50.0, 2.0)
        return if (subjectList.isNotEmpty()) {
            subjectList.filter { it.id == examData.subjectId }[0]
        } else {
            errorSubject
        }
    }

    fun isExamExpired(examLongDate: Long): Boolean {
        val examDate = FormatDate.formatLongToDate(time = examLongDate)
        if (Calendar.getInstance().time.after(examDate)) {
            return true
        }
        return false
    }
}