package com.cronelab.riscc.ui.questionnaire.questionnaire_instances

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.cronelab.riscc.ui.questionnaire.model.AnswerSVM
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerObj
import kotlinx.android.synthetic.main.fragment_data_input_questionnaire.*

class DataInputQuestionnaire : Fragment() {
    val TAG = "DataInputQuestionnaire"
    private lateinit var dbQuestion: DBQuestions
    private lateinit var answerSVM: AnswerSVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data_input_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbQuestion = arguments?.get("questionObj") as DBQuestions
        titleTV.text = dbQuestion.title
        descriptionTV.text = dbQuestion.body
        answerSVM = ViewModelProvider(requireActivity()).get(AnswerSVM::class.java)
        answerET.addTextChangedListener(inputDataTextWatcher)

        Log.w(TAG, "groupQuestionnaireId: ${dbQuestion.groupQuestionnaireId}")
    }

    private val inputDataTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty()) {
                val answer = Answer()
                answer.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()

                val answerList = ArrayList<AnswerObj>()
                val answerObj = AnswerObj()
                answerObj.answer = s.toString()
                answerObj.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()
                answerObj.questionId = dbQuestion.id.toString()
                answerList.add(answerObj)

                answer.answerList = answerList
                answerSVM.sendAnswer(answer)
            } else {
                answerSVM.sendAnswer(null)
            }
        }
    }
}