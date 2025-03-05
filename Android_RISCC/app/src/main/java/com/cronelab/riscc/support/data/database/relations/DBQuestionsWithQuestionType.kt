//package com.cronelab.diary.support.data.database.relations
//
//import androidx.room.Embedded
//import androidx.room.Relation
//import com.cronelab.diary.support.data.database.table.questions.DBQuestions
//import com.cronelab.diary.support.data.database.table.questions.QuestionType
//
//data class DBQuestionsWithQuestionType(
//    @Embedded val dbQuestions: DBQuestions,
//    @Relation(parentColumn = "id", entityColumn = "parentId") val questionType: QuestionType
//)