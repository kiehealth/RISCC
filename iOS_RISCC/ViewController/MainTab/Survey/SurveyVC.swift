//
//  SurveyVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift
import WebKit
import DropDown

class SurveyVC: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, SideMenuVCDelegate {

    @IBOutlet weak var btnMenu: UIButton!
    @IBOutlet weak var btnProfile: UIButton!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var lblPage: UILabel!
    @IBOutlet weak var btnPrevious: UIButton!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var btnSkipWidth: NSLayoutConstraint!
    

    // Empty Message
    @IBOutlet weak var viewEmptyMessage: UIView!
    @IBOutlet weak var lblEmptyMessage: UILabel!
    
    @IBOutlet weak var viewActiveQuestionnaire: UIView!
    @IBOutlet weak var lblQuestionnaire: UILabel!
    @IBOutlet weak var imgDownArrow: UIImageView!
    
    var questionList = NSMutableArray.init()
    var currentShownQuestionIndex = 0
    var isLastQuestionOfSelectedQuestionnaire: Bool = false
    var dropDown = DropDown()
    var activeQuestionnaires = NSMutableArray.init()
    var selectedActiveQuestionnaire = DBActiveQuestionnaire.init()
    let surveyViewModel = SurveyViewModel()

    var welcomeMessage: String? = nil
    var thankYouMessage: String? = nil
    var isThankYouMessage = false
    
    @IBOutlet weak var viewMessage: UIView!
    @IBOutlet weak var btnOk: UIButton!
    @IBOutlet weak var lblWelcomeOrThankYou: RISCCUILabel!


    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.btnNext.isHidden = true
        self.btnPrevious.isHidden = true
        self.viewEmptyMessage.isHidden = true
        self.btnMenu.setImage(UIImage.init(named: "ic_menu")?.imageWithColor(color: .white), for: .normal)
        self.btnPrevious.setTitleColor(.init(hexString: Constants.Color.COLOR_BASE), for: .normal)
        self.btnNext.setTitleColor(.init(hexString: Constants.Color.COLOR_BASE), for: .normal)
        self.lblPage.textColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        self.viewActiveQuestionnaire.layer.borderColor = UIColor.init(hexString: Constants.Color.COLOR_BORDER).cgColor
        self.lblEmptyMessage.text = NSLocalizedString("No questions available for now!", comment: "")
    }
    
    fileprivate func loadData() {
        self.viewMessage.isHidden = true
        self.loadActiveQuestionnaire()
    }
    
    fileprivate func loadQuestions() {
        DBQuestionHandler.shared.getAllQuestions(success: { (questions) in
            self.questionList = questions
            self.collectionView.reloadData()
            
            DispatchQueue.main.async {
                let nextButtonTitle = self.questionList.count == 1 ? NSLocalizedString("Finish", comment: "") : NSLocalizedString("Next", comment: "")
                self.btnNext.setTitle(nextButtonTitle, for: .normal)
            
                self.viewEmptyMessage.isHidden = !(self.questionList.count == 0)
                self.btnNext.isHidden = (self.questionList.count == 0)
                self.lblPage.isHidden = (self.questionList.count == 0)
                

                if self.questionList.count == 0 {
                    self.lblEmptyMessage.text = NSLocalizedString("No questions available for now!", comment: "")
                    self.btnPrevious.isHidden = true
                } else {
                    self.collectionView.isHidden = false
                    if self.currentShownQuestionIndex > 0 {
                        self.currentShownQuestionIndex = 0
                        self.btnPrevious.isHidden = true
                    }
                    self.collectionView.scrollToItem(at: IndexPath(row: 0, section: 0), at: .left, animated: false)
                }
            }
        }) { (message) in
            print(message)
        }
        self.viewEmptyMessage.isHidden = true
    }
    
    fileprivate func loadActiveQuestionnaire() {
        self.showLoading(view: self.view, text: "")
        APIQuestionHandler.shared.requestActiveGroupQuestionnaries { (message, activeQuestionnaires) in
            self.activeQuestionnaires = activeQuestionnaires
            self.viewEmptyMessage.isHidden = (self.activeQuestionnaires.count > 0)
            if self.activeQuestionnaires.count > 0 {
                self.imgDownArrow.isHidden = (self.activeQuestionnaires.count == 1)
                let activeQuestionnaire = self.activeQuestionnaires[0] as! DBActiveQuestionnaire
                self.selectedActiveQuestionnaire = activeQuestionnaire
                self.lblQuestionnaire.text = self.selectedActiveQuestionnaire.title!
                self.loadUnansweredQuestionsForQuestionnaire(dbActiveQuestionnaire: self.selectedActiveQuestionnaire)
            } else {
                self.lblQuestionnaire.text = NSLocalizedString("--Select Questionnaire--", comment: "")
                self.loadQuestions()
                self.imgDownArrow.isHidden = true
                self.lblEmptyMessage.text = NSLocalizedString("No questions available for now!", comment: "")
                self.stopLoading(fromView: self.view)
                
                if self.activeQuestionnaires.count <= 0 { // No active questionnaire so move to Answer Screen
                    self.answer()
                }
            }
        } failure: { (message) in
            self.stopLoading(fromView: self.view)
            print(message)
        }
    }
    
    fileprivate func loadUnansweredQuestionsForQuestionnaire(dbActiveQuestionnaire: DBActiveQuestionnaire) {
        APIQuestionHandler.shared.requestQuestionUnansweredByQuestionnaire(groupId:dbActiveQuestionnaire.groupId, questionnaierId: dbActiveQuestionnaire.questionnaireId) { (message, welcomeText, thankYouText) in
            self.welcomeMessage = welcomeText
            self.thankYouMessage = thankYouText
            self.show(welcomeOrThankYou: self.welcomeMessage ?? "", isThakYouMessage: false)

            self.stopLoading(fromView: self.view)
            self.loadQuestions()
        } failure: { (message) in
            self.stopLoading(fromView: self.view)
            print(message)
        }
    }
    
    fileprivate func isValid()->Bool {
        return true
    }
    
    private func showLoading(view:UIView, text:String){
        let loadingNotification = MBProgressHUD.showAdded(to: view, animated: true)
        loadingNotification.mode = MBProgressHUDMode.indeterminate
        loadingNotification.label.text = text
    }
    
    private func stopLoading(fromView:UIView){
        MBProgressHUD.hide(for: fromView, animated: true)
    }
    
    private func saveAnswerToServer(answers:[[String: Any]], isNext:Bool) {
        let questionnaire = DBQuestionnaireHandler.shared.getQuestionnaire()
        let postParameter = NSMutableDictionary.init()
        postParameter.setValue(self.selectedActiveQuestionnaire.questionId, forKey: "groupQuestionnaireId")
        postParameter.setValue(answers, forKey: "answers")
        self.showLoading(view: self.view, text: "")
        APIAnswerHandler.shared.answer(postParameter: postParameter, success: { (message) in
            print(message)
            self.stopLoading(fromView: self.view)
            DBAnswersHandler.shared.deleteAllAnswers()
            Util.clearQuestionOrAnswerData()
            Util.postAnalytics(analyticsType: ANALYTICS_TYPE.YourAnswerOut.rawValue)
            if isNext {
                self.nextPage()
            } else {
                self.previousPage()
            }
        }) { (message) in
            self.stopLoading(fromView: self.view)
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    private func show(welcomeOrThankYou message: String, isThakYouMessage: Bool){
        self.isThankYouMessage = isThakYouMessage
        self.viewMessage.isHidden = false
        self.lblWelcomeOrThankYou.text = message
    }
    
    private func nextPage() {
        if self.currentShownQuestionIndex < (self.questionList.count - 1) && !self.isLastQuestionOfSelectedQuestionnaire { // Can load next question
            let indexPath = IndexPath.init(row: self.currentShownQuestionIndex + 1, section: 0)
            self.collectionView.scrollToItem(at: indexPath, at: .right, animated: true)
        } else { // This is the last question shown
            self.btnNext.isHidden = true
            self.showLoading(view: self.view, text: "")
            APIQuestionHandler.shared.requestFinishGroupQuestionnaier(groupQuestionnaireId: self.selectedActiveQuestionnaire.groupId) { (message) in
                self.stopLoading(fromView: self.view)
                self.show(welcomeOrThankYou: self.thankYouMessage ?? "", isThakYouMessage: true)
            } failure: { (message) in
                self.stopLoading(fromView: self.view)
            }
        }
    }
    
    private func previousPage() {
        if self.currentShownQuestionIndex > 0 {
            let indexPath: IndexPath = IndexPath(item: self.currentShownQuestionIndex - 1, section: 0)
            self.collectionView.scrollToItem(at: indexPath, at: .left, animated: true)
        }
    }

    @objc private func alertControllerBackgroundTapped() {
        self.dismiss(animated: true, completion: nil)
    }
    
    private func answeredData(isNextButtonPressed:Bool) {
        let question = DiaryHandler.shared.question
        let questionOption = DiaryHandler.shared.questionOption
        let questionOptionList = DiaryHandler.shared.questionOptionList
        if question != nil && question?.answer != "" && question?.questionType.title == QUESTION_TYPE.DataInput.rawValue {
            var answers = [[String: Any]]()
            let answer = ["answer": question?.answer, "questionId": question?.questionId!]
            answers.append(answer)
            DiaryHandler.shared.question = nil
            self.saveAnswerToServer(answers: answers, isNext: isNextButtonPressed)
        } else if self.surveyViewModel.isAnswerForSingleSlectionTypeQuestion() {
            if questionOption?.answer != "" { //If there is free text input in screen.
                var answerOptions = [[String: Any]]()
                let answerOption = ["questionOptionId": questionOption?.questionOptionId!, "value": questionOption?.answer]
                answerOptions.append(answerOption)
                
                var answers = [[String: Any]]()
                let answer = ["answerOptions": answerOptions, "questionId": question?.questionId!] as [String : Any]
                answers.append(answer)
                DiaryHandler.shared.question = nil
                DiaryHandler.shared.questionOption = nil
                self.saveAnswerToServer(answers: answers, isNext: isNextButtonPressed)
            } else {
                var answerOptions = [[String: Any]]()
                let answerOption = ["questionOptionId": questionOption?.questionOptionId!]
                answerOptions.append(answerOption as [String : Any])
                
                var answers = [[String: Any]]()
                let answer = ["answerOptions": answerOptions, "questionId": (question?.questionId!)! as Any] as [String : Any]
                answers.append(answer)
                
                DiaryHandler.shared.question = nil
                DiaryHandler.shared.questionOption = nil
                self.saveAnswerToServer(answers: answers, isNext: isNextButtonPressed)
            }
        } else if self.surveyViewModel.isOneAnswerSelectedFromMultipleSlectionTypeQuestion() {
            var answerOptions = [[String: Any]]()
            let answerOption = ["questionOptionId": questionOption?.questionOptionId!]
            answerOptions.append(answerOption as [String : Any])
            
            var answers = [[String: Any]]()
            let answer = ["answer": questionOption?.answer as Any, "answerOptions": answerOptions, "questionId": question?.questionId! as Any] as [String : Any]
            answers.append(answer)
            
            DiaryHandler.shared.question = nil
            DiaryHandler.shared.questionOption = nil
            self.saveAnswerToServer(answers: answers, isNext: isNextButtonPressed)
        } else if self.surveyViewModel.isMoreThanOneAnswerSelectedFromMultipleSlectionTypeQuestion() {
            print("QuestionList: \(questionOptionList.count)")
            var answerOptions = [[String: Any]]()
            for case let questionOption as DBQuestionOption in questionOptionList {
                let answerOption = ["questionOptionId": questionOption.questionOptionId!]
                answerOptions.append(answerOption)
            }
            var answers = [[String: Any]]()
            let answer = ["answerOptions": answerOptions, "questionId": (question?.questionId!)! as Any] as [String : Any]
            answers.append(answer)
            
            DiaryHandler.shared.question = nil
            DiaryHandler.shared.questionOption = nil
            DiaryHandler.shared.questionOptionList = NSMutableArray.init()
            self.saveAnswerToServer(answers: answers, isNext: isNextButtonPressed)
            
        } else if self.surveyViewModel.isAnswerForRatingTypeQuestion() {
            var answerOptions = [[String: Any]]()
            let answerOption = ["questionOptionId": questionOption?.questionOptionId!]
            answerOptions.append(answerOption as [String : Any])
            
            var answers = [[String: Any]]()
            let answer = ["answerOptions": answerOptions, "questionId": question?.questionId! as Any] as [String : Any]
            answers.append(answer)
            DiaryHandler.shared.question = nil
            DiaryHandler.shared.questionOption = nil
            self.saveAnswerToServer(answers: answers, isNext: isNextButtonPressed)
        } else { // There is no answered data so simply navigate to next question.
            if isNextButtonPressed {
                self.nextPage()
            } else {
                self.previousPage()
            }
        }
    }
    
    private func moveToWebViewVC(header:String, url:String, pdfSource:String) {
        let webViewVC = UIStoryboard.init(name: Constants.Storyboard.UTILITY, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.WEB_VIEW_VC) as! WebViewVC
        webViewVC.header = header
        webViewVC.url = url
        webViewVC.pdfSource = pdfSource
        self.navigationController?.pushViewController(webViewVC, animated: false)
    }
    
    private func logout() {
        APIAnalyticsHandler.shared.postAppAnalytics(analyticsType: ANALYTICS_TYPE.UserOut.rawValue, success: { (message) in
            let viewControllers: [UIViewController] = (self.tabBarController?.navigationController?.viewControllers)!
            for aViewController in viewControllers {
                if(aViewController is LoginVC){
                    Util.clearDataBase()
                    self.tabBarController?.navigationController!.popToViewController(aViewController, animated: false);
                }
            }
        }, failure: { (message) in
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        })
    }
    
    private func dropDownView(view:UIView, options:[String]) {
        self.dropDown.anchorView = view
        self.dropDown.bottomOffset = CGPoint(x: 0, y: self.viewActiveQuestionnaire.bounds.height)
        self.dropDown.cellHeight = self.viewActiveQuestionnaire.bounds.height
        self.dropDown.dataSource = options
        self.dropDown.show()
        self.dropDown.selectionAction = {(index: Int, item: String) in
            
            if view == self.viewActiveQuestionnaire {
                let activeQuestionnaire = self.activeQuestionnaires.object(at: index) as! DBActiveQuestionnaire
                if self.selectedActiveQuestionnaire.questionId != activeQuestionnaire.questionId {
                    self.selectedActiveQuestionnaire = activeQuestionnaire
                    self.lblQuestionnaire.text = item
                    self.loadUnansweredQuestionsForQuestionnaire(dbActiveQuestionnaire: self.selectedActiveQuestionnaire)
                }
            }
        }
    }
    
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnMenuAction(_ sender: Any) {
        if(self.presentedViewController == nil){
            self.tabBarController?.tabBar.isHidden = true
            let viewController = UIStoryboard.init(name: Constants.Storyboard.SIDE_MENU, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.SIDE_MENU_VC) as! SideMenuVC
//            viewController.view.backgroundColor = #colorLiteral(red: 0.9882352941, green: 0.9882352941, blue: 0.9882352941, alpha: 0.17)
//            viewController.modalPresentationStyle = .overCurrentContext
//            viewController.modalTransitionStyle = .coverVertical
            viewController.modalPresentationStyle = .fullScreen
            viewController.delegate = self
            self.present(viewController, animated: false, completion: nil)
        } else {
            print("No need to present again")
        }
    }
    
    @IBAction func btnProfileAction(_ sender: Any) {
        
    }
    
    @IBAction func btnPreviousAction(_ sender: Any) {
        self.answeredData(isNextButtonPressed: false)
    }
    
    @IBAction func btnNextAction(_ sender: Any) {
        self.answeredData(isNextButtonPressed: true)
    }
    
    @IBAction func viewActiveQuestionnaierAction(_ sender: Any) {
        let options: [String] = self.activeQuestionnaires.map({(($0 as? DBActiveQuestionnaire)?.title)!})
        self.dropDownView(view: self.viewActiveQuestionnaire, options: options)
    }
    
    @IBAction func btnOkAction(_ sender: Any) {
        self.viewMessage.isHidden = !self.viewMessage.isHidden
        if self.isThankYouMessage {
            self.isThankYouMessage = false
            loadActiveQuestionnaire()
        }
    }
    
    // MARK: -
    // MARK: Object Methods
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func loadView() {
        super.loadView()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.configureView()
        self.loadData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        Util.clearQuestionOrAnswerData()
        self.loadActiveQuestionnaire()
        
        if DiaryHandler.shared.isFromNotification {
            DiaryHandler.shared.isFromNotification = false
            self.loadData()
        }
        
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        
        if DiaryHandler.shared.moveToNotificaitonVC {
            DiaryHandler.shared.moveToNotificaitonVC = false
            self.notification()
        }
        
        let countryCode = Locale.current.languageCode
        if countryCode == "sv" {
            self.btnSkipWidth.constant = 100
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.YourAnswerIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.YourAnswerOut.rawValue)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
    }
    
    // MARK: -
    // MARK: Delegate Methods
    
    // MARK: -
    // MARK: UICollectionViewDataSource, UICollectionViewDelegate Methods
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.questionList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let question = self.questionList[indexPath.row] as! DBQuestion
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.CellIdentifier.QUESTIONNER, for: indexPath) as! CellQuestioner
        cell.loadQuestion(question: question)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: self.collectionView.frame.width, height: self.collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        self.lblPage.text = "\(indexPath.row + 1)/\(self.questionList.count)"
        self.btnPrevious.isHidden = indexPath.row > 0 ? false : true
        self.btnNext.setTitle(indexPath.row == (self.questionList.count - 1) ? NSLocalizedString("Finish", comment: "") : NSLocalizedString("Next", comment: ""), for: .normal)
        self.isLastQuestionOfSelectedQuestionnaire = (indexPath.row == (self.questionList.count - 1))
        self.currentShownQuestionIndex = indexPath.row
    }
    
    // MARK: -
    // MARK: SideMenuVCDelegate Methods
    
    func close() {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func survey() {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func notification() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Notification.rawValue
    }
    
    func note() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Note.rawValue
    }
    
    func links() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Link.rawValue
    }
    
    func feedback() {
        self.tabBarController?.tabBar.isHidden = false
        let viewController = UIStoryboard.init(name: Constants.Storyboard.FEEDBACK, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.FEEDBACK_VC) as! FeedbackVC
        self.navigationController?.pushViewController(viewController, animated: false)
    }
    
    func answer() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Answer.rawValue
    }
    
    func contactUs() {
        self.tabBarController?.tabBar.isHidden = false
        let viewController = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.CONTACT_US_VC) as! ContactUsVC
        viewController.modalTransitionStyle = .crossDissolve
        viewController.modalPresentationStyle = .fullScreen
        self.present(viewController, animated: false, completion: nil)
        
    }
    
    func privacyPolicy() {
        DispatchQueue.main.async {
            let dbResouceFile = DBResourceFileHandler.shared.getPrivacyPolicyResourceFile()
            let urlString = "\(URL_BASE)\(dbResouceFile.url ?? "")"
            if URL(string: urlString) != nil {
                self.moveToWebViewVC(header: dbResouceFile.desc ?? "Privacy Policy", url: urlString, pdfSource: "")
            } else { return }
        }
    }
    
    func about() {
        DispatchQueue.main.async {
            self.tabBarController?.tabBar.isHidden = false
            let dbResouceFile = DBResourceFileHandler.shared.getAboutUsResourceFile()
            let urlString = "\(URL_BASE)\(dbResouceFile.url ?? "")"
            if URL(string: urlString) != nil {
                self.moveToWebViewVC(header: dbResouceFile.desc ?? "About Us", url: urlString, pdfSource: "")
            } else { return }
        }
    }
    
    func changePassword() {
        self.tabBarController?.tabBar.isHidden = false
        let changePasswordVC = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.CHANGE_PASSWORD_VC) as! ChangePasswordVC
        changePasswordVC.modalPresentationStyle = .fullScreen
        self.present(changePasswordVC, animated: false, completion: nil)
    }
    
    func logOut() {
        let viewControllers: [UIViewController] = (self.tabBarController?.navigationController?.viewControllers)!
        for aViewController in viewControllers {
            if(aViewController is LoginVC){
                Util.clearDataBase()
                DispatchQueue.main.async {
                    self.tabBarController?.navigationController!.popToViewController(aViewController, animated: false);
                    
                }
            }
        }
    }
}
