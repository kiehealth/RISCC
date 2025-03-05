//
//  EditAnswerVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Cosmos

class EditAnswerVC: UIViewController, UITableViewDataSource, UITableViewDelegate, CellAnsweredQuestionTypeSelectOneDelegate, CellAnsweredQuestionTypeSelectMoreDelegate {

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblParentTitle: UILabel!
    @IBOutlet weak var lblParentTitleTopConstraints: NSLayoutConstraint!
    @IBOutlet weak var lblParentBody: UILabel!
    @IBOutlet weak var lblParentBodyTopConstraints: NSLayoutConstraint!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblBody: UILabel!
    @IBOutlet weak var tblAnsweredQuestionType: UITableView!
    @IBOutlet weak var viewRating: CosmosView!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var btnUpdate: UIButton!
    
//    var answeredQuestion: DBAnsweredQuestion? = nil
    var isOptionSelected: Bool = false
    var questionTypeSelectOneIndex: Int = 0
    var questionTypeSelectMoreIndex: [Int] = []
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.lblParentTitleTopConstraints.constant = 0
        self.lblParentBodyTopConstraints.constant = 0
        self.btnUpdate.isHidden = true
    }
    
    fileprivate func loadData() {
//        if answeredQuestion?.answeredParentQuestion != nil {
//            self.lblParentTitle.text = answeredQuestion?.answeredParentQuestion?.title!
//            self.lblParentTitleTopConstraints.constant = 16.0
//            self.lblParentBody.text = answeredQuestion?.answeredParentQuestion?.body!
//            self.lblParentBodyTopConstraints.constant = 8.0
//        } else {
//            self.lblParentTitle.text = ""
//            self.lblParentTitleTopConstraints.constant = 0.0
//            self.lblParentBody.text = ""
//            self.lblParentBodyTopConstraints.constant = 0.0
//        }
//
//
//        self.lblTitle.text = "\((answeredQuestion?.displayOrder)!). \((answeredQuestion?.title)!)"
//        self.lblBody.text = answeredQuestion?.body!
//
//        if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.Rating.rawValue {
//            self.tblAnsweredQuestionType.isHidden = true
//            self.viewRating.settings.totalStars = (self.answeredQuestion?.answeredQuestionOptions.count)!
//            self.viewRating.settings.fillMode = .full
//
//            var ratingSize: Double = 0.0
//            let answer = self.answeredQuestion?.answerList[0] as! DBAnswer
//            for case let answeredQuestionOption as DBAnsweredQuestionOption in (self.answeredQuestion?.answeredQuestionOptions)! {
//                ratingSize += 1
//                if answer.title! == answeredQuestionOption.title! {
//                    break
//                }
//            }
//            self.viewRating.rating = ratingSize
//            self.viewRating.didTouchCosmos = { rating in
//                DiaryHandler.shared.answeredQuestion = self.answeredQuestion
//                DiaryHandler.shared.answeredQuestionRating = rating
//                var answeredQuestionOptions = self.answeredQuestion?.answeredQuestionOptions[Int(rating) - 1] as! DBAnsweredQuestionOption
//                print("QuestionOption: \(answeredQuestionOptions.title!)")
//                DiaryHandler.shared.answeredQuestionOption = answeredQuestionOptions
//            }
//        } else {
//            self.tblAnsweredQuestionType.isHidden = false
//        }
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
    
    private func loadAnswerApi(answers:[[String: Any]]) {
        
        let postParameter = NSMutableDictionary.init()
        postParameter.setValue(answers, forKey: "answers")
        
        self.showLoading(view: self.view, text: "Saving Answer")
        APIAnswerHandler.shared.answer(postParameter: postParameter, success: { (message) in
            print(message)
            self.stopLoading(fromView: self.view)
            Util.clearQuestionOrAnswerData()
        }) { (message) in
            self.stopLoading(fromView: self.view)
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
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
        self.dismiss(animated: false, completion: nil)
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
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
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
    // MARK: UITableViewDataSource, UITableViewDelegate Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
//        if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.DataInput.rawValue {
//            return 1
//        } else if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectOne.rawValue {
//            return (answeredQuestion?.answeredQuestionOptions.count)!
//        } else if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectMore.rawValue {
//            return (answeredQuestion?.answeredQuestionOptions.count)!
//        } else {
            return 0
//        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.DataInput.rawValue {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "CellAnsweredQuestionTypeDataInput", for: indexPath) as! CellAnsweredQuestionTypeDataInput
//            cell.load(answeredQuestion: answeredQuestion!)
//            return cell
//
//        } else if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectOne.rawValue {
//            let answeredQuestionOption = answeredQuestion?.answeredQuestionOptions[indexPath.row] as! DBAnsweredQuestionOption
//            let cell = tableView.dequeueReusableCell(withIdentifier: "CellAnsweredQuestionTypeSelectOne", for: indexPath) as! CellAnsweredQuestionTypeSelectOne
//            cell.delegate = self
//            cell.loadQuestionOption(answeredQuestionOption: answeredQuestionOption, answeredQuestion: self.answeredQuestion!)
//            return cell
//
//        } else if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectMore.rawValue {
//            let answeredQuestionOption = answeredQuestion?.answeredQuestionOptions[indexPath.row] as! DBAnsweredQuestionOption
//            let cell = tableView.dequeueReusableCell(withIdentifier: "CellAnsweredQuestionTypeSelectMore", for: indexPath) as! CellAnsweredQuestionTypeSelectMore
//            cell.delegate = self
//            cell.loadQuestionOption(answeredQuestionOption: answeredQuestionOption, answeredQuestion: self.answeredQuestion!)
//            return cell
//
//        } else {
            return UITableViewCell.init()
//        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectOne.rawValue {
//            let cell = tableView.cellForRow(at: indexPath) as! CellAnsweredQuestionTypeSelectOne
//            cell.select()
//            self.questionTypeSelectOneIndex = indexPath.row
//        } else if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectMore.rawValue {
//            //            self.isOptionSelected = !self.isOptionSelected
//            let cell = tableView.cellForRow(at: indexPath) as! CellAnsweredQuestionTypeSelectMore
//            print("Selected: \(cell.optionSelected)")
//            if cell.optionSelected {
//                cell.select()
////                self.questionTypeSelectMoreIndex.append(indexPath.row)
//            } else {
////                if let index = self.questionTypeSelectMoreIndex.index(of: indexPath.row) {
////                    self.questionTypeSelectMoreIndex.remove(at: index)
////                }
//                cell.deselect()
//            }
//        }
    }
    
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
//        if self.answeredQuestion?.answeredQuestionType.title == QUESTION_TYPE.SelectOne.rawValue {
//            let cell = tableView.cellForRow(at: indexPath) as! CellAnsweredQuestionTypeSelectOne
//            cell.deselect()
//
//        }
    }
    
    // MARK: -
    // MARK: CellQuestionTypeSelectOneDelegate Methods
    
    func textFieldTapped() {
        let indexPath = IndexPath(row: self.questionTypeSelectOneIndex, section: 0)
        print("IndexPath: \(indexPath)")
        self.tblAnsweredQuestionType.delegate?.tableView!(tblAnsweredQuestionType, didDeselectRowAt: indexPath)
    }
    
    // MARK: -
    // MARK: CellQuestionTypeSelectMoreDelegate Methods
    
    func questionTypeSelectMoreTextFieldTapped() {
        self.questionTypeSelectMoreIndex.forEach { (index) in
            let indexPath = IndexPath(row: index, section: 0)
            self.tblAnsweredQuestionType.delegate?.tableView!(tblAnsweredQuestionType, didSelectRowAt: indexPath)
        }
    }

}
