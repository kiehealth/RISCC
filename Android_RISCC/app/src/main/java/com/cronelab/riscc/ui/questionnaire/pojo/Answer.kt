package com.cronelab.riscc.ui.questionnaire.pojo

import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("groupQuestionnaireId") var groupQuestionnaireId: String = "",
    @SerializedName("answers") var answerList: List<AnswerObj> = ArrayList()
)

data class AnswerObj(
    //@SerializedName("id") var id: String = "",
    @SerializedName("answer") var answer: String = "",
    @SerializedName("groupQuestionnaireId") var groupQuestionnaireId: String = "",
    @SerializedName("questionId") var questionId: String = "",
    @SerializedName("answerOptions") var answerOptionList: List<AnswerOption> = ArrayList()
)

data class AnswerOption(
    //@SerializedName("id") var id: String = "",
    @SerializedName("questionOptionId") var questionOptionId: String = "",
    @SerializedName("value") var value: String = ""
)
