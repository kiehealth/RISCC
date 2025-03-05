package com.cronelab.riscc.ui.questionnaire.questionnaire_instances

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.cronelab.riscc.ui.questionnaire.model.AnswerSVM
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerObj
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerOption
import kotlinx.android.synthetic.main.fragment_single_selection_questionnaire.*
import kotlin.collections.ArrayList

class SingleSelectionQuestionnaire : Fragment() {

    private lateinit var dbQuestion: DBQuestions
    private lateinit var answerSVM: AnswerSVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_single_selection_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbQuestion = arguments?.get("questionObj") as DBQuestions
        setData()
        answerSVM = ViewModelProvider(requireActivity()).get(AnswerSVM::class.java)
    }

    private fun setData() {
        radioGroupParentLayout.removeAllViews()
        parentQuestionBodyTV.visibility = View.GONE
        parentQuestionTitleTV.visibility = View.GONE
        titleTV.text = dbQuestion.title
        if (!dbQuestion.body.isNullOrEmpty()) {
            descriptionTV.text = dbQuestion.body
        } else {
            descriptionTV.visibility = View.GONE
        }
        val optionList = dbQuestion.dbQuestionOptions
        val radioGroup = RadioGroup(requireContext())
        radioGroup.orientation = RadioGroup.VERTICAL
        var inputView: View? = null

        if (!optionList.isNullOrEmpty()) {
            for (option in optionList) {
                if (option.optionTitle.equals("other", ignoreCase = true)) {
                    val layoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    inputView = layoutInflater.inflate(R.layout.other_answer_layout, null)
                } else {
                    val radioBtn = RadioButton(requireContext())
                    radioBtn.text = option.optionTitle
                    radioBtn.setPadding(10, 10, 10, 10)
                    radioGroup.addView(radioBtn)
                }
            }
            radioGroupParentLayout.addView(radioGroup)
            if (inputView != null) {
                radioGroupParentLayout.addView(inputView)
            }

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val rb = radioGroup.findViewById<RadioButton>(checkedId)
                if (rb != null) {
                    val answerTitle = rb.text.toString()
                    val answer = Answer()
                    answer.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()

                    val answerObjList = ArrayList<AnswerObj>()
                    val answerObj = AnswerObj()
                    //answerObj.answer = answerTitle
                    answerObj.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()
                    answerObj.questionId = dbQuestion.id.toString()

                    val answerOptionList = ArrayList<AnswerOption>()
                    val answerOption = AnswerOption()
                    for (option in optionList) {
                        if (option.optionTitle.equals(answerTitle, ignoreCase = true)) {
                            answerOption.questionOptionId = option.questionOptionId.toString()
                            answerOption.value = option.optionValue.toString()
                            answerOptionList.add(answerOption)
                        }
                    }
                    answerObj.answerOptionList = answerOptionList
                    answerObjList.add(answerObj)
                    answer.answerList = answerObjList
                    answerSVM.sendAnswer(answer)
                }
            }
        }

    }


}