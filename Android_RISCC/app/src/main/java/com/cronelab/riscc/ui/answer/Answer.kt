package com.cronelab.riscc.ui.answer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.data.database.table.answer.Answer
import com.cronelab.riscc.support.data.responses.GroupQuestionnaireApiResponse.GroupQuestionnaire
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.PdfViewer
import com.cronelab.riscc.ui.WebViewer
import com.cronelab.riscc.ui.answer.model.AnswerViewModel
import com.cronelab.riscc.ui.answer.model.AnswerViewModelFactory
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireViewModel
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_answer.*
import kotlinx.android.synthetic.main.fragment_questionnaire.spinner
import kotlinx.android.synthetic.main.fragment_questionnaire.spinnerTitleTV
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Answer : Fragment(), KodeinAware, View.OnClickListener {

    private val TAG = "AnswerActivity"
    override val kodein by kodein()

    private var dbUser: DBUser? = null

    private val answerViewModelFactory: AnswerViewModelFactory by instance()
    private lateinit var answerViewModel: AnswerViewModel

    private lateinit var questionIdTemp:String
    private lateinit var moreInfoForRisccCalculatedTemp:String

    private val questionnaireViewModelFactory: QuestionnaireViewModelFactory by instance()
    private lateinit var questionnaireViewModel: QuestionnaireViewModel

    private var questionnaireTitleListWindow: ListPopupWindow? = null
    private val answerList = ArrayList<Answer>()
    private val answerAdapter = AnsweredAdapter(answerList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        answerViewModel = ViewModelProviders.of(this, answerViewModelFactory).get(AnswerViewModel::class.java)
        questionnaireViewModel = ViewModelProviders.of(this, questionnaireViewModelFactory).get(QuestionnaireViewModel::class.java)
        dbUser = User.getInstance(requireContext()).user
        configureAnswerRecyclerView()
        swipeRefreshLayout.isRefreshing = true
        getAnswers()
        initObservers()
        initListeners()
    }

//    override fun onResume() {
//        super.onResume()
//        getAnswers()
//
//    }

    private fun initListeners() {
        spinner.setOnClickListener(this)
        swipeRefreshLayout.setOnRefreshListener {
            getAnswers()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.spinner -> {
                questionnaireTitleListWindow?.show()
            }

        }

    }

    private fun configureAnswerRecyclerView() {
        with(answerRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = answerAdapter
        }
    }

    private fun initObservers() {
        questionnaireViewModel.groupQuestionnaireResponse.observe(viewLifecycleOwner) {
            questionnaireViewModel.groupQuestionnaireResponse.removeObserver { }
            if (isVisible) {
                Log.e("veryImp","we got questionsGroup ${Gson().toJson(it)}")
                /*progressDialog.dismiss()*/
                dismissSwipeRefreshing()
                if (it.error == null) {
                    val questionnaireMap = HashMap<String, GroupQuestionnaire>()
                    for (questionGroup in it.groupQuestionnaireList) {
                        questionnaireMap[questionGroup.questionnaire!!.title] = questionGroup
                    }
                    Log.e("veryImp","questionnaireMap is given by $ ${Gson().toJson(questionnaireMap)}")
                    val questionnaireTitleList = questionnaireMap.keys.toList().sorted()
                    Log.e("veryImp","sorted questionnaireMap title is given by $questionnaireTitleList")
                    prepareQuestionnaireTitleWindow(questionnaireTitleList, questionnaireMap)
                } else {
                    requireActivity().showToast(it.message)
                }
            }
        }
        answerViewModel.answerResponse.observe(viewLifecycleOwner) {
            answerViewModel.answerResponse.removeObserver { }

            if (it.error == null) {

                if (it.answerList.isNotEmpty()) {
                    answerAdapter.updateAnswer(it.answerList)
//                    answerViewModel.getCalculatedRisccForGivenQuestionnaireFromServer(dbUser!!,questionIdTemp)
                    Log.e("veryImp","now we got the answer ${Gson().toJson(it)}")
                    //answerAdapter.updateAnswer(it.answerList)
                    noActiveQuestionnaireTV.visibility = View.GONE
                } else {
                    noActiveQuestionnaireTV.visibility = View.VISIBLE
                    showRisccMessage.visibility = View.GONE
                }
            } else {
                Log.i(TAG, it.message)
            }
        }
        answerViewModel.calculatedRisccResponse.observe(viewLifecycleOwner) {
            answerViewModel.calculatedRisccResponse.removeObserver{}
            Log.e("veryImp","now we got the riscc value  ${Gson().toJson(it)}")
            if (it.error == null) {
                showRisccMessageContainer.visibility = View.VISIBLE
                showRisccMessage.text = it.message
                moreInfoForRisccCalculatedTemp = it.moreInfo
                if(it.moreInfo.isNotEmpty()){
                    moreInfoIcon.visibility =  View.VISIBLE
                }
            } else
            {
                showRisccMessageContainer.visibility = View.GONE
            }
            showRisccMessageContainer.setOnClickListener{
                Log.e("veryImp","button is clicked")
                val intent =  Intent(requireActivity(),WebViewer::class.java)
                intent.putExtra("Url",moreInfoForRisccCalculatedTemp)
                startActivity(intent)
            }
        }
    }

    private fun dismissSwipeRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getAnswers() {
        dbUser?.let { user ->
            /* val progressDialog = ProgressDialog.loadingProgressDialog(requireContext(), getString(R.string.loadingAnsweredQuestions))
             progressDialog.show()*/
            questionnaireViewModel.getGroupQuestionnaire(user)

        }
    }

    private fun prepareQuestionnaireTitleWindow(questionnaireTitleList: List<String>, questionnaireMap: HashMap<String, GroupQuestionnaire>) {
        questionnaireTitleListWindow = ListPopupWindow(requireActivity())
        questionnaireTitleListWindow?.let {
            it.anchorView = spinner
            it.isModal = true
            it.width = ListPopupWindow.MATCH_PARENT
            it.height = ListPopupWindow.WRAP_CONTENT
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, questionnaireTitleList)
            it.setAdapter(adapter)
            if (questionnaireTitleList.isNotEmpty()) {
                noActiveQuestionnaireTV.visibility = View.GONE
                spinnerTitleTV.text = questionnaireTitleList[0]
                val firstQuestionnaire = questionnaireMap[questionnaireTitleList[0]]
                if (dbUser != null && firstQuestionnaire != null) {
                    Log.e("veryImp","for question id ${Gson().toJson(firstQuestionnaire)} in ${Gson().toJson(questionnaireTitleList)}")
                    questionIdTemp = firstQuestionnaire.questionnaire?.id.toString()
                    answerViewModel.getAnswerFromServer(dbUser!!, firstQuestionnaire)
                    answerViewModel.getCalculatedRisccForGivenQuestionnaireFromServer(dbUser!!,firstQuestionnaire.questionnaire?.id.toString())
                }
            } else {
                noActiveQuestionnaireTV.visibility = View.VISIBLE
                spinnerTitleTV.text = ""
                dropDownIV.visibility = View.GONE
            }

            it.setOnItemClickListener { _, _, position, _ ->
                it.dismiss()
                val key = questionnaireTitleList[position]
                spinnerTitleTV.text = key
                val groupQuestionnaire = questionnaireMap[key]
                if (dbUser != null && groupQuestionnaire != null) {

                    answerViewModel.getAnswerFromServer(dbUser!!, groupQuestionnaire)
                }
            }
        }
    }


}