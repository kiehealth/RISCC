//
//  AnsweredQuestionVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import DropDown

class AnsweredQuestionVC: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var viewQuestionnaire: UIView!
    @IBOutlet weak var lblQuestionnaire: UILabel!
    @IBOutlet weak var imgDownArrow: UIImageView!
    
    var answerList = NSMutableArray.init()
    var questionnaires = NSMutableArray.init()
    var selectedQuestionnaire = DBActiveQuestionnaire.init()
    var dropDown = DropDown()
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.lblHeader.text = NSLocalizedString("Your Answer", comment: "")
    }
    
    fileprivate func loadData() {
        self.loadGroupQuestionnaire()
    }
    
    fileprivate func loadAnsweredQuestion() {
        self.answerList = DBAnswerDataHandler.shared.getAnswers()
        self.tableView.reloadData()
    }
    
    fileprivate func loadGroupQuestionnaire() {
        APIQuestionHandler.shared.requestGroupQuestionnaire { (message, questionnaires) in
            self.questionnaires = questionnaires
            print("Questionnaires: \(questionnaires.count)")
            if self.questionnaires.count > 0 {
                self.imgDownArrow.isHidden = self.questionnaires.count == 1 ? true : false
                let questionnaierActive = self.questionnaires[0] as! DBActiveQuestionnaire
                self.selectedQuestionnaire = questionnaierActive
                print("QuestionIID: \(self.selectedQuestionnaire.questionId)")
                self.lblQuestionnaire.text = self.selectedQuestionnaire.title!
                self.loadAnswerByGroupQuestionnaire()
            } else {
                self.imgDownArrow.isHidden = true
            }
        } failure: { (message) in
            print(message)
        }
    }
    
    fileprivate func loadAnswerByGroupQuestionnaire() {
        APIQuestionHandler.shared.requestAnswerByGroupQuestionnaier(groupQuestionnaireId: self.selectedQuestionnaire.questionId) { (message) in
            print(message)
            self.loadAnsweredQuestion()
        } failure: { (message) in
            print(message)
        }

    }
    
    fileprivate func isValid()->Bool {
        return true
    }
    
    private func showLoading(view:UIView, text:String){
        let loadingNotification = MBProgressHUD.showAdded(to: view, animated: true)
        loadingNotification.mode = MBProgressHUDMode.indeterminate
        //loadingNotification.labelText = text
    }
    
    private func stopLoading(fromView:UIView){
        MBProgressHUD.hide(for: fromView, animated: true)
    }
    
    private func dropDownView(view:UIView, options:[String]) {
        self.dropDown.anchorView = view
        self.dropDown.bottomOffset = CGPoint(x: 0, y: self.viewQuestionnaire.bounds.height)
        self.dropDown.cellHeight = self.viewQuestionnaire.bounds.height
        self.dropDown.dataSource = options
        self.dropDown.show()
        self.dropDown.selectionAction = {(index: Int, item: String) in
            
            if view == self.viewQuestionnaire {
                let questionnaire = self.questionnaires.object(at: index) as! DBActiveQuestionnaire
                if self.selectedQuestionnaire.questionId != questionnaire.questionId {
                    self.selectedQuestionnaire = questionnaire
                    self.lblQuestionnaire.text = item
                    self.loadAnswerByGroupQuestionnaire()
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
    
    @IBAction func btnBackAction(_ sender: Any) {
//        self.dismiss(animated: false, completion: nil)
        self.navigationController?.popViewController(animated: false)
    }
    
    @IBAction func viewQuestionnaierAction(_ sender: Any) {
        let options: [String] = self.questionnaires.map({(($0 as? DBActiveQuestionnaire)?.title)!})
        self.dropDownView(view: self.viewQuestionnaire, options: options)
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
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
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
    // MARK: UITableViewDataSource, UITableViewDelegate Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.answerList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let answer = self.answerList[indexPath.row] as! DBAnswerData
        let cell = tableView.dequeueReusableCell(withIdentifier: "CellAnsweredQuestion", for: indexPath) as! CellAnsweredQuestion
        cell.load(answer: answer)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let answer = self.answerList[indexPath.row] as! DBAnswerData
//        let editAnswerVC = UIStoryboard.init(name: Constants.Storyboard.ANSWER, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.EDIT_ANSWER_VC) as! EditAnswerVC
//        editAnswerVC.answeredQuestion = answeredQuestion
//        self.present(editAnswerVC, animated: false) {
            //
//        }
    }
    

}
