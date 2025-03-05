package com.cronelab.riscc.ui.questionnaire

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.common.utils.NumberAwareStringComparator
import com.cronelab.riscc.support.common.utils.Utils
import com.cronelab.riscc.support.constants.QuestionnaireType
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.data.database.table.questions.DBActiveQuestionnaireGroup
import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import com.cronelab.riscc.ui.questionnaire.adapter.QuestionnaireAdapter
import com.cronelab.riscc.ui.questionnaire.model.AnswerSVM
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireViewModel
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireViewModelFactory
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import com.cronelab.riscc.ui.questionnaire.pojo.WelcomeQuestionnaire
import com.cronelab.riscc.ui.questionnaire.questionnaire_instances.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_questionnaire.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Questionnaire : Fragment(), KodeinAware, View.OnClickListener {


    private val TAG = "Questionnaire"
    override val kodein by kodein()

    var dbUser: DBUser? = null

    private lateinit var questionnaireViewModel: QuestionnaireViewModel
    private val questionnaireViewModelFactory: QuestionnaireViewModelFactory by instance()

    private lateinit var answerSVM: AnswerSVM
    private lateinit var questionListTemp: List<DBQuestions>
    private lateinit var welcomeMessageInfoTemp: WelcomeQuestionnaire
    private var questionnaireTitleListWindow: ListPopupWindow? = null
    var questionnaireTitleList: List<String> = ArrayList()
    private  var questionHasToBeLoaded = true
    var pageCounter = 0
    var totalQuestionSize = 0
    var answer: Answer? = null
    var isFinished = false

    var progressDialog: Dialog? = null
    var selectedQuestionnaireGroup: DBActiveQuestionnaireGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        dbUser = User.getInstance(requireContext()).user
        questionnaireViewModel = ViewModelProviders.of(requireActivity(), questionnaireViewModelFactory).get(QuestionnaireViewModel::class.java)
        answerSVM = ViewModelProvider(requireActivity()).get(AnswerSVM::class.java)
        getActiveQuestionnaire()
        initListener()
        initObservers()
    }

    private fun initUI() {
        viewpager.disableScroll(true)
        previousBtn.visibility = View.INVISIBLE
        previousBtn.isClickable = false
        nextBtn.visibility = View.INVISIBLE
        nextBtn.isClickable = false
    }

    private fun initListener() {
        spinner.setOnClickListener(this)
        nextBtn.setOnClickListener(this)
        previousBtn.setOnClickListener(this)
        proceed.setOnClickListener(this)
    }

    private fun reset() {
        pageCounter = 0
        totalQuestionSize = 0
        nextTV.text = getString(R.string.next)
        previousBtn.visibility = View.INVISIBLE
        previousBtn.isClickable = false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.spinner -> {
                if (questionnaireTitleList.isNotEmpty()) {
                    questionnaireTitleListWindow?.show()
                } else {
                    requireActivity().showToast(getString(R.string.no_active_questionnaire_group))
                }
            }

            R.id.nextBtn -> {
                postAnswer()
                Utils.hideKeyboardFrom(requireContext(), questionnaireRootView)
                if (nextTV.text.toString().equals(getString(R.string.finish), ignoreCase = true)) {
                    pageCounter++
                    viewpager.currentItem = pageCounter
                    isFinished = true
                    reset()
                } else {
                    pageCounter++
                    viewpager.currentItem = pageCounter
                    val pageIndex = "$pageCounter/$totalQuestionSize"
                    pageIndexTV.text = pageIndex
                    if (pageCounter < totalQuestionSize) {
                        nextBtn.isClickable = true
                        nextBtn.visibility = View.VISIBLE
                        previousBtn.visibility = View.VISIBLE
                        previousBtn.isClickable = true
                    } else {
                        nextTV.setText(R.string.finish)
                        previousBtn.visibility =  View.INVISIBLE
                    }

                }
            }

            R.id.previousBtn -> {
                postAnswer()
                Utils.hideKeyboardFrom(requireContext(), questionnaireRootView)
                pageCounter--
                viewpager.currentItem = pageCounter
                val pageIndex = "$pageCounter/$totalQuestionSize"
                pageIndexTV.text = pageIndex
                if (pageCounter < 2) {
                    previousBtn.isClickable = false
                    previousBtn.visibility = View.INVISIBLE
                } else {
                    previousBtn.isClickable = true
                    previousBtn.visibility = View.VISIBLE
                }
                nextTV.text = resources.getString(R.string.next)
                nextBtn.isClickable = true
                nextBtn.visibility = View.VISIBLE
            }
            R.id.proceed ->{
                disableWelcomeMessage()
//                if (proceedText.text.toString().equals(getString(R.string.ok), ignoreCase = true)) {
                if (questionHasToBeLoaded) {
                    questionHasToBeLoaded = false
                    Log.d(TAG, "proceed button clicked with ok text ")
                    configureQuestionnaireView(questionListTemp)

                }else{
                    Log.d(TAG, "proceed button clicked with proceed text ")
                    getActiveQuestionnaire()
                }

            }
        }
    }

    private fun postAnswer() {
        if (answer != null) {
            Log.d(TAG, "Answer is not null")
            questionnaireViewModel.postAnswerToServer(dbUser!!, answer!!)
            questionnaireViewModel.answerPostResponse.observe(this) {
                if (it.error == null) {
                    answer = null
                    if (isFinished) {
                        isFinished = false
                        finishQuestionnaire()
                        //getActiveQuestionnaire()
                        //show thank you screen
                        //thankYouTV.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            Log.d(TAG, "Answer is null")
            if (nextTV.text.toString().equals(getString(R.string.finish), ignoreCase = true)) {
                finishQuestionnaire()
                //getActiveQuestionnaire()
                //show thank you screen
                //thankYouTV.visibility = View.VISIBLE
            }
        }
    }

    private fun finishQuestionnaire() {
        selectedQuestionnaireGroup?.let {
            showProgressDialog(getString(R.string.finishingQuestionnaire))
            questionnaireViewModel.finishGroupQuestionnaire(dbUser!!, it)
        }
    }

    private fun getActiveQuestionnaire() {
        if (dbUser != null) {
            thankYouTV.visibility = View.GONE
            questionnaireViewModel.getActiveQuestionnaire(dbUser!!)
            val progressDialog = ProgressDialog.loadingProgressDialog(requireContext(), getString(R.string.loading_questionnaire))
            progressDialog.show()
            questionnaireViewModel.activeQuestionnaireResponse.observe(viewLifecycleOwner) {
                Log.e("VeryImp","question is given by ${Gson().toJson(it.activeQuestionnaireGroupList)}")
                if (isVisible) {
                    progressDialog.dismiss()
                    questionnaireViewModel.activeQuestionnaireResponse.removeObserver { }
                    if (it.error == null) {
                        Log.e("VeryImp","error is not null")
                        if (it.activeQuestionnaireGroupList.isEmpty()) {
                            Log.e("VeryImp","active question is empty ")

                            noActiveQuestionnaireTV.visibility = View.VISIBLE
                            hideProgressDialog()
                            val navController = Navigation.findNavController(requireView())
                            navController.navigate(R.id.navigation_answer)
                            return@observe
                        }
                        val questionnaireMap = HashMap<String, DBActiveQuestionnaireGroup>()
                        for (activeGroup in it.activeQuestionnaireGroupList) {
                            questionnaireMap[activeGroup.questionnaire!!.title] = activeGroup
                        }
                        questionnaireTitleList = questionnaireMap.keys.toList().sorted()
                        Log.e("VeryImp","list of title of questionnarie is $questionnaireTitleList")
                        if (questionnaireTitleList.size < 2) {
                            Log.e("VeryImp","size is less than 2")
                            dropDownIV.visibility = View.GONE
                        }

                        if (it.activeQuestionnaireGroupList.isEmpty()) {
                            Log.e("VeryImp","active question is empty ")

                            noActiveQuestionnaireTV.visibility = View.VISIBLE

                        } else {
                            Log.e("VeryImp","active question is not emtpy ")

                            prepareQuestionnaireTitleWindow(questionnaireTitleList, questionnaireMap)
                            noActiveQuestionnaireTV.visibility = View.GONE
                        }

                    } else {
                        requireActivity().showToast(it.message)
                    }
                }
            }
        }

    }


    private fun initObservers() {
        questionnaireViewModel.questionsResponse.observe(viewLifecycleOwner) {
            Log.w(TAG, "questionsResponse obj: ${Gson().toJson(it)}")

            if (isVisible) {
                hideProgressDialog()
                if (it!!.error == null) {
                    questionnaireViewModel.questionsResponse.removeObserver { }
                    welcomeMessageInfoTemp = it.welcomeMessageInfo!!
                    if (it!!.questionList.isNotEmpty()) {
                        Log.e(TAG, "questionsResponse obj: ${Gson().toJson(it!!.questionList)}")
                        Log.e(TAG, "welcometext obj: ${Gson().toJson(it.welcomeMessageInfo?.welcomeText)}")
//                        welcomeMessageInfoTemp = it.welcomeMessageInfo!!
                        showWelcomeMessage(it!!.questionList,it.welcomeMessageInfo?.welcomeText)


//                        configureQuestionnaireView(it!!.questionList)
                    } else {
//                        hideProgressDialog()
//                        val navController = Navigation.findNavController(requireView())
//                        navController.navigate(R.id.navigation_answer)
                        Log.w(TAG, "questionList is empty")
//                        return@observe
                    }
                    //questionnaireViewModel.questionsResponse.removeObserver { }
                }
            }
        }

        answerSVM.answer.observe(viewLifecycleOwner) {
            Log.w(TAG, "answer obj: ${Gson().toJson(it)}")
            answer = it
        }

        questionnaireViewModel.questionnaireFinishResponse.observe(viewLifecycleOwner) {
            if (isVisible) {
                //requireActivity().showToast(it.message)
                hideProgressDialog()
                showThankYouMessage()
//                getActiveQuestionnaire()
            }
        }
    }

    private fun showThankYouMessage() {
        welcomeMessageContainer.visibility = View.VISIBLE
        welcomeMessage.text = welcomeMessageInfoTemp.thankYouText
        proceedText.text = getString(R.string.ok)
        spinner.visibility = View.GONE
        viewpager.visibility = View.GONE
        controllerLayout.visibility = View.GONE
    }

    private fun showWelcomeMessage(questionList: List<DBQuestions>, welcomeText: String?) {
        questionListTemp  = questionList
        welcomeMessageContainer.visibility = View.VISIBLE
        proceedText.text = getString(R.string.ok)
        welcomeMessage.text = welcomeText
        spinner.visibility = View.GONE
        viewpager.visibility = View.GONE
        controllerLayout.visibility = View.GONE

    }
    private fun disableWelcomeMessage() {
        welcomeMessageContainer.visibility = View.GONE
        spinner.visibility = View.VISIBLE
        viewpager.visibility = View.VISIBLE
        controllerLayout.visibility = View.VISIBLE

    }


    private fun configureQuestionnaireView(questionList: List<DBQuestions>) {
        if (checkQuestion(questionList)) {
            val sortedQuestionList = sortQuestion(questionList)
            reset()
            viewpager.removeAllViews()
            controllerLayout.visibility = View.VISIBLE
            noQuestionnaireTV.visibility = View.GONE
            thankYouTV.visibility = View.GONE

            val pagerAdapter = QuestionnaireAdapter(childFragmentManager)
            pagerAdapter.addFragment(QuestionnaireIntroduction())

            //for (dbQuestions in questionList) {
            for (dbQuestions in sortedQuestionList) {
                var bundle: Bundle
                when (dbQuestions.dbQuestionType!!.questionTypeTitle) {
                    QuestionnaireType.DATA_INPUT -> {
                        val dataInputQuestionnaireInstance = DataInputQuestionnaire()
                        bundle = Bundle()
                        bundle.putSerializable("questionObj", dbQuestions)
                        dataInputQuestionnaireInstance.arguments = bundle
                        pagerAdapter.addFragment(dataInputQuestionnaireInstance)
                    }

                    QuestionnaireType.SELECT_ONE -> {
                        val singleSelectionQuestionnaireInstance = SingleSelectionQuestionnaire()
                        bundle = Bundle()
                        bundle.putSerializable("questionObj", dbQuestions)
                        singleSelectionQuestionnaireInstance.arguments = bundle
                        pagerAdapter.addFragment(singleSelectionQuestionnaireInstance)
                    }

                    QuestionnaireType.SELECT_MORE_THAN_ONE -> {
                        val multipleSelectionQuestionnaireInstance = MultipleSelectionQuestionnaire()
                        bundle = Bundle()
                        bundle.putSerializable("questionObj", dbQuestions)
                        multipleSelectionQuestionnaireInstance.arguments = bundle
                        pagerAdapter.addFragment(multipleSelectionQuestionnaireInstance)
                    }

                    QuestionnaireType.RATING -> {
                        val ratingQuestionnaireInstance = RatingQuestionnaire()
                        bundle = Bundle()
                        bundle.putSerializable("questionObj", dbQuestions)
                        ratingQuestionnaireInstance.arguments = bundle
                        pagerAdapter.addFragment(ratingQuestionnaireInstance)
                    }

                    QuestionnaireType.NONE -> {
                        val nonAnswerableQuestionnaireInstance = NonAnswerableQuestionnaire()
                        bundle = Bundle()
                        bundle.putSerializable("questionObj", dbQuestions)
                        nonAnswerableQuestionnaireInstance.arguments = bundle
                        pagerAdapter.addFragment(nonAnswerableQuestionnaireInstance)
                    }
                }
            }
            viewpager.adapter = pagerAdapter
            viewpager.offscreenPageLimit = viewpager.adapter!!.count
            viewpager.currentItem = 1
            pageCounter++
            totalQuestionSize = questionList.size
            pageIndexTV.text = "$pageCounter/$totalQuestionSize"
            nextBtn.visibility = View.VISIBLE
            pageIndexTV.visibility = View.VISIBLE

            if (totalQuestionSize == 1) {
                nextTV.text = resources.getString(R.string.finish)
            }
        } else {
            viewpager.removeAllViews()
            controllerLayout.visibility = View.GONE
            noQuestionnaireTV.visibility = View.VISIBLE
        }
    }

    private fun prepareQuestionnaireTitleWindow(questionnaireTitleList: List<String>, questionnaireMap: HashMap<String, DBActiveQuestionnaireGroup>) {
        if (questionnaireTitleList.isNotEmpty()) {
            questionnaireTitleListWindow = ListPopupWindow(requireActivity())
            questionnaireTitleListWindow?.let {
                it.anchorView = spinner
                it.isModal = true
                it.width = ListPopupWindow.MATCH_PARENT
                it.height = ListPopupWindow.WRAP_CONTENT
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, questionnaireTitleList)
                it.setAdapter(adapter)
                spinnerTitleTV.text = questionnaireTitleList[0]
                val firstQuestionnaire = questionnaireMap[questionnaireTitleList[0]]
                selectedQuestionnaireGroup = firstQuestionnaire
                if (dbUser != null && firstQuestionnaire != null) {
                    showProgressDialog(getString(R.string.loadingQuestionList))
                    questionnaireViewModel.getQuestionsFromServer(dbUser!!, firstQuestionnaire)
                }
                it.setOnItemClickListener { _, _, position, _ ->
                    it.dismiss()
                    viewpager.removeAllViews()
                    reset()
                    val key = questionnaireTitleList[position]
                    spinnerTitleTV.text = key
                    val questionnaire = questionnaireMap[key]
                    selectedQuestionnaireGroup = questionnaire
                    if (dbUser != null && questionnaire != null) {
                        showProgressDialog(getString(R.string.loadingQuestionList))
                        questionnaireViewModel.getQuestionsFromServer(dbUser!!, questionnaire)
                        thankYouTV.visibility = View.GONE

                    }
                }
            }
        }
    }

    private fun checkQuestion(questionList: List<DBQuestions>): Boolean {
        var hasAnswerableQuestion = false
        for (question in questionList) {
            if (!question.dbQuestionType!!.questionTypeTitle.equals(QuestionnaireType.NONE, false)) {
                hasAnswerableQuestion = true
                break

            }
        }
        return hasAnswerableQuestion
    }


    private fun sortQuestion(questionList: List<DBQuestions>): ArrayList<DBQuestions> {
        val questionMap = HashMap<String, DBQuestions>()
        for (questionItem in questionList) {
            //var displayOrder = "${questionItem.dbQuestionQuestionnaires!![0].displayOrder!!}${questionItem.title}"
            var displayOrder = ""
            for (question in questionItem.dbQuestionQuestionnaires!!) {
                if (question.questionnaire?.title!!.equals(selectedQuestionnaireGroup?.questionnaire?.title, ignoreCase = true)) {
                    displayOrder = "${question.displayOrder!!}${questionItem.title}"
                    break
                }
            }
            questionMap[displayOrder] = questionItem
        }
        val keys: List<String> = questionMap.keys.toList()
        //val sortedKeys = keys.sortedBy { it }
        val comparator = NumberAwareStringComparator()
        val sortedKeys = keys as MutableList<String> //Changed here
        sortedKeys.sortWith(comparator)
        val sortedHashMap = LinkedHashMap<String, DBQuestions>()
        val sortedQuestionList = ArrayList<DBQuestions>()
        for (value in sortedKeys) {
            Log.w(TAG, "Sorted Keys: $value")
            sortedHashMap[value] = questionMap[value]!!
            sortedQuestionList.add(questionMap[value]!!)
        }
        return sortedQuestionList
    }

    private fun showProgressDialog(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.loadingProgressDialog(requireContext(), message)
        }
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
                progressDialog = null
            }
        }
    }

}