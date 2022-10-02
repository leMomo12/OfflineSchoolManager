package com.mnowo.offlineschoolmanager.feature_exam.domain.use_case.util

import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.feature_todo.domain.models.ToDo

object ToDoSubjectItemHelper {

    fun getSubjectItem(toDo: ToDo, subjectList: List<Subject>): Subject {
        val errorSubject = Subject(-1, "", 0, "fds", 50.0, 50.0, 2.0)
        return if (subjectList.isNotEmpty()) {
            subjectList.filter { it.id == toDo.subjectId }[0]
        } else {
            errorSubject
        }
    }
}