package com.cronelab.riscc.ui.questionnaire.questionnaire_instances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import kotlinx.android.synthetic.main.fragment_non_answarable_questionnaire.*

class NonAnswerableQuestionnaire : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_non_answarable_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentQuestionBodyTV.visibility = View.GONE
        parentQuestionTitleTV.visibility = View.GONE
        val dbQuestion = arguments?.get("questionObj") as DBQuestions

        titleTV.setText(dbQuestion.title)
        descriptionTV.setText(dbQuestion.body)
    }
}