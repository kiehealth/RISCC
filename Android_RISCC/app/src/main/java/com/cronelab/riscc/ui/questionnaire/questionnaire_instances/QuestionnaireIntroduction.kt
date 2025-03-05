package com.cronelab.riscc.ui.questionnaire.questionnaire_instances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cronelab.riscc.R

class QuestionnaireIntroduction : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questionnaire_introduction, container, false)
    }


}