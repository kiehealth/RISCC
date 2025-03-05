//
//  CellQuestionTypeSelectOne.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellQuestionTypeSelectOneDelegate: class {
    func textFieldTapped()
    func optionAutoSelect(indexPath:IndexPath)
}

class CellQuestionTypeSelectOne: UITableViewCell {

    @IBOutlet weak var viewOption: UIView!
    @IBOutlet weak var lblOption: UILabel!
    @IBOutlet weak var viewOther: UIView!
    @IBOutlet weak var lblOptionOther: UILabel!
    @IBOutlet weak var txtAnswer: UITextField!
    
    var question: DBQuestion = DBQuestion.init()
    var questionOption = DBQuestionOption.init()
    let answer = AnswerModel.init()
    var delegate: CellQuestionTypeSelectOneDelegate? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.viewOption.layer.borderColor = UIColor.darkGray.cgColor
        self.lblOptionOther.text = NSLocalizedString("Other", comment: "")
        self.txtAnswer.placeholder = NSLocalizedString("Your Answer", comment: "")
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func loadQuestionOption(questionOption:DBQuestionOption, question:DBQuestion, indexPath:IndexPath) {
        self.questionOption = questionOption
        self.question = question
        self.answer.question = question
        self.answer.questionOption = questionOption
        if questionOption.title! != "OTHER" {
            self.lblOption.text = questionOption.title!
            self.viewOther.isHidden = true
        } else {
            self.lblOptionOther.text = NSLocalizedString("Other", comment: "")
            self.viewOther.isHidden = false
        }
        
        if self.questionOption.answer != " " {
            self.txtAnswer.text = self.questionOption.answer
        }
        
        if self.questionOption.isSelected {
            DispatchQueue.main.async {
//                self.select()
                self.delegate?.optionAutoSelect(indexPath: indexPath)
            }
        }
    }
    
    func select() {
        self.txtAnswer.resignFirstResponder()
        self.txtAnswer.text = ""
        self.viewOption.backgroundColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        self.questionOption.isSelected = true
        DiaryHandler.shared.question = self.question
        DiaryHandler.shared.questionOption = self.questionOption
    }
    
    func deselect() {
        self.questionOption.isSelected = false
        self.txtAnswer.text = ""
        self.viewOption.backgroundColor = UIColor.white
    }
    
    func deselectBeforeLoading() {
        self.txtAnswer.text = ""
        self.viewOption.backgroundColor = UIColor.white
    }
    
    @IBAction func txtAnswerEditingChanged(_ sender: Any) {
        self.questionOption.answer = self.txtAnswer.text!
        DiaryHandler.shared.question = self.question
        DiaryHandler.shared.questionOption = self.questionOption
    }

    @IBAction func txtAnswerEditingBegin(_ sender: Any) {
        DiaryHandler.shared.question = nil
        DiaryHandler.shared.questionOption = nil
        self.delegate?.textFieldTapped()
    }
    
}
