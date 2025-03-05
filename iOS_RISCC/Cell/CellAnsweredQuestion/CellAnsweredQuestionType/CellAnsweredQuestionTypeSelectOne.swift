//
//  CellAnsweredQuestionTypeSelectOne.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellAnsweredQuestionTypeSelectOneDelegate: class {
    func textFieldTapped()
}

class CellAnsweredQuestionTypeSelectOne: UITableViewCell {

    @IBOutlet weak var viewOption: UIView!
    @IBOutlet weak var lblOption: UILabel!
    @IBOutlet weak var viewOther: UIView!
    @IBOutlet weak var lblOptionOther: UILabel!
    @IBOutlet weak var txtAnswer: UITextField!
    
    var answeredQuestion: DBAnsweredQuestion = DBAnsweredQuestion.init()
//    var answeredQuestionOption = DBAnsweredQuestionOption.init()
    var delegate: CellAnsweredQuestionTypeSelectOneDelegate? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.viewOption.layer.borderColor = UIColor.darkGray.cgColor
        self.lblOptionOther.text = NSLocalizedString("Other", comment: "")
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
//    func loadQuestionOption(answeredQuestionOption:DBAnsweredQuestionOption, answeredQuestion:DBAnsweredQuestion) {
//        self.answeredQuestionOption = answeredQuestionOption
//        self.answeredQuestion = answeredQuestion
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
//        if answeredQuestionOption.title! == "OTHER" && isOptionFound {
//            self.txtAnswer.text = answeredQuestion.answer
//            self.txtAnswer.placeholder = NSLocalizedString("Your Answer", comment: "")
//        }
//    }
    
    func select() {
        self.txtAnswer.resignFirstResponder()
        self.txtAnswer.text = ""
        self.viewOption.backgroundColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        DiaryHandler.shared.answeredQuestion = self.answeredQuestion
//        DiaryHandler.shared.answeredQuestionOption = self.answeredQuestionOption
    }
    
    func deselect() {
        self.txtAnswer.text = ""
        self.viewOption.backgroundColor = UIColor.white
    }
    
    @IBAction func txtAnswerEditingChanged(_ sender: Any) {
//        self.answeredQuestionOption.answer = self.txtAnswer.text!
        DiaryHandler.shared.answeredQuestion = self.answeredQuestion
//        DiaryHandler.shared.answeredQuestionOption = self.answeredQuestionOption
    }
    
    @IBAction func txtAnswerEditingBegin(_ sender: Any) {
        DiaryHandler.shared.answeredQuestion = nil
//        DiaryHandler.shared.answeredQuestionOption = nil
        self.delegate?.textFieldTapped()
    }

}
