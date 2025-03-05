//
//  CellQuestionTypeSelectMore.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellAnsweredQuestionTypeSelectMoreDelegate: class {
    func questionTypeSelectMoreTextFieldTapped()
}

class CellAnsweredQuestionTypeSelectMore: UITableViewCell {

    @IBOutlet weak var viewOption: UIView!
    @IBOutlet weak var lblOption: UILabel!
    @IBOutlet weak var lblOptionOther: UILabel!
    @IBOutlet weak var viewOther: UIView!
    @IBOutlet weak var txtAnswer: UITextField!
    
    var answeredQuestion = DBAnsweredQuestion.init()
//    var answeredQuestionOption = DBAnsweredQuestionOption.init()
    var optionSelected: Bool = true
    var delegate: CellAnsweredQuestionTypeSelectMoreDelegate? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.viewOption.layer.borderColor = UIColor.darkGray.cgColor
        self.lblOptionOther.text = NSLocalizedString("Other", comment: "")
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
//    func loadQuestionOption(answeredQuestionOption:DBAnsweredQuestionOption, answeredQuestion: DBAnsweredQuestion) {
//        self.answeredQuestion = answeredQuestion
//        self.answeredQuestionOption = answeredQuestionOption
//        if answeredQuestionOption.title! != "OTHER" {
//            self.lblOption.text = answeredQuestionOption.title!
//            self.viewOther.isHidden = true
//        } else {
//            self.lblOptionOther.text = NSLocalizedString("Other", comment: "")
//            self.viewOther.isHidden = false
//        }
//
//        var isOptionFound: Bool = false
//        for case let answer as DBAnswer in self.answeredQuestion.answerList {
//            if self.answeredQuestionOption.title! == answer.title {
//                self.select()
//                isOptionFound = true
//            }
//        }
//
//        if !isOptionFound && answeredQuestionOption.title! == "OTHER" {
//            self.txtAnswer.text = answeredQuestion.answer
//            self.select()
//        }
//    }
    
    func select() {
        self.optionSelected = false
        self.viewOption.backgroundColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        DiaryHandler.shared.answeredQuestion = self.answeredQuestion
//        DiaryHandler.shared.answeredQuestionOptionList.add(self.answeredQuestionOption)
        print("QuestionCount: \(DiaryHandler.shared.answeredQuestionOptionList.count)")
    }
    
    func deselect() {
        self.optionSelected = true
        self.viewOption.backgroundColor = UIColor.white
        
        if DiaryHandler.shared.answeredQuestionOptionList.count > 0 {
//            let index = DiaryHandler.shared.answeredQuestionOptionList.index(of: self.answeredQuestionOption)
//            DiaryHandler.shared.answeredQuestionOptionList.removeObject(at: index)
        }
    }
    
    @IBAction func txtAnswerEditingChanged(_ sender: Any) {
//        self.answeredQuestionOption.answer = self.txtAnswer.text!
        DiaryHandler.shared.answeredQuestion = self.answeredQuestion
//        DiaryHandler.shared.answeredQuestionOption = self.answeredQuestionOption
    }
    
    @IBAction func txtAnswerEditingDidBegin(_ sender: Any) {
        self.delegate?.questionTypeSelectMoreTextFieldTapped()
    }

}
