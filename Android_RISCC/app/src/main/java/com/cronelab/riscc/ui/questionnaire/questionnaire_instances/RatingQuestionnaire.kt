package com.cronelab.riscc.ui.questionnaire.questionnaire_instances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.cronelab.riscc.ui.questionnaire.model.AnswerSVM
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerObj
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerOption
import kotlinx.android.synthetic.main.fragment_data_input_questionnaire.descriptionTV
import kotlinx.android.synthetic.main.fragment_data_input_questionnaire.titleTV
import kotlinx.android.synthetic.main.fragment_rating_questionnaire.*
import kotlin.math.ceil

class RatingQuestionnaire : Fragment() {

    private lateinit var dbQuestion: DBQuestions
    private lateinit var answerSVM: AnswerSVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbQuestion = arguments?.get("questionObj") as DBQuestions
        setData()
        answerSVM = ViewModelProvider(requireActivity()).get(AnswerSVM::class.java)
    }

    private fun setData() {
        titleTV.text = dbQuestion.title
        descriptionTV.text = dbQuestion.body
        val optionList = dbQuestion.dbQuestionOptions
        if (!optionList.isNullOrEmpty()) {
            ratingBar.numStars = optionList.size
            ratingBar.max = optionList.size
            val optionTitleList = ArrayList<String>()
            for (option in optionList) {
                optionTitleList.add(option.optionTitle!!)
            }

            ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    ratingBar.rating = Math.ceil(rating.toDouble()).toFloat()
                    val answer = Answer()
                    answer.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()

                    val answerObjList = ArrayList<AnswerObj>()
                    val position = ceil(rating.toDouble()).toInt() - 1

                    val answerObj = AnswerObj()
                   // answerObj.answer = optionTitleList[position]
                    answerObj.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()
                    answerObj.questionId = dbQuestion.id.toString()

                    val answerOptionList = ArrayList<AnswerOption>()

                    val answerOption = AnswerOption()
                    answerOption.questionOptionId = dbQuestion.dbQuestionOptions?.get(position)?.questionOptionId.toString()
                    answerOption.value = dbQuestion.dbQuestionOptions?.get(position)?.optionValue.toString()
                    answerOptionList.add(answerOption)

                    answerObj.answerOptionList = answerOptionList

                    answerObjList.add(answerObj)
                    answer.answerList = answerObjList

                    answerSVM.sendAnswer(answer)

                }
            }
        }
    }
}