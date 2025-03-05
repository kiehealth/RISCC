package com.cronelab.riscc.ui.questionnaire.questionnaire_instances

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.cronelab.riscc.ui.questionnaire.model.AnswerSVM
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerObj
import com.cronelab.riscc.ui.questionnaire.pojo.AnswerOption
import kotlinx.android.synthetic.main.fragment_multiple_selection_questionnaire.*

class MultipleSelectionQuestionnaire : Fragment() {

    private lateinit var dbQuestion: DBQuestions
    private lateinit var answerSVM: AnswerSVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_multiple_selection_questionnaire, container, false)
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
        checkboxParentLayout.removeAllViews()
        var inputView: View? = null
        val optionList = dbQuestion.dbQuestionOptions
        if (!optionList.isNullOrEmpty()) {
            val answer = Answer()
            answer.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()
            val answerOptionList = ArrayList<AnswerOption>()


            for (option in optionList) {
                if (option.optionTitle.equals("other", ignoreCase = true)) {
                    val layoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    inputView = layoutInflater.inflate(R.layout.other_answer_layout, null)
                } else {
                    val checkBox = CheckBox(context)
                    checkBox.text = option.optionTitle
                    checkBox.setPadding(10, 10, 10, 10)
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            checkBox.isChecked = isChecked
                        }
                        val answerTitle = checkBox.text.toString()
                        val answerObjList = ArrayList<AnswerObj>()
                        val answerObj = AnswerObj()
                        //answerObj.answer = answerTitle
                        answerObj.groupQuestionnaireId = dbQuestion.groupQuestionnaireId.toString()
                        answerObj.questionId = dbQuestion.id.toString()

                        val answerOption = AnswerOption()

                        for (optionItem in optionList) {
                            if (optionItem.optionTitle.equals(answerTitle, ignoreCase = true)) {
                                answerOption.questionOptionId = optionItem.questionOptionId.toString()
                                answerOption.value = optionItem.optionValue.toString()
                                if (isChecked) {
                                    answerOptionList.add(answerOption)
                                } else {
                                    val status = answerOptionList.remove(answerOption)
                                }
                            }
                        }
                        answerObj.answerOptionList = answerOptionList
                        answerObjList.add(answerObj)
                        answer.answerList = answerObjList
                        if (answerObj.answerOptionList.isNotEmpty()) {
                            answerSVM.sendAnswer(answer)
                        } else {
                            answerSVM.sendAnswer(null)
                            Log.w("Multiple selection", "answerOptionList is empty: ")
                        }
                    }

                   /* val checkboxRoot: LinearLayout  =  LinearLayout(requireContext())
                    checkboxRoot.setPadding(10,10,10,10)
                    checkboxRoot.addView(checkBox)
                    checkboxParentLayout.addView(checkboxRoot)*/

                    checkboxParentLayout.addView(checkBox)
                }
            }

            if (inputView != null) {
                inputView.setPadding(10, 10, 10, 10)
                checkboxParentLayout.addView(inputView)
            }
        }
    }
}