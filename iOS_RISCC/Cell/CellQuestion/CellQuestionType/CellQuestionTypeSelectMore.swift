//
//  CellQuestionTypeSelectMore.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellQuestionTypeSelectMoreDelegate: class {
    func questionTypeSelectMoreTextFieldTapped(indexPath:IndexPath)
}

class CellQuestionTypeSelectMore: UITableViewCell {

    @IBOutlet weak var viewOption: UIView!
    @IBOutlet weak var lblOption: UILabel!
    @IBOutlet weak var lblOptionOther: UILabel!
    @IBOutlet weak var viewOther: UIView!
    @IBOutlet weak var txtAnswer: UITextField!
    
    var question = DBQuestion.init()
    var questionOption = DBQuestionOption.init()
    var optionSelected: Bool = true
    let answer = AnswerModel.init()
    var delegate: CellQuestionTypeSelectMoreDelegate? = nil
    var indexPath: IndexPath = IndexPath.init()
    
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
    
    func loadQuestionOption(questionOption:DBQuestionOption, question: DBQuestion, indexPath:IndexPath) {
        self.indexPath = indexPath
        self.question = question
        self.questionOption = questionOption
        self.answer.question = question
        self.answer.questionOption = questionOption
        if questionOption.title! != "OTHER" {
            self.lblOption.text = questionOption.title!
            self.viewOther.isHidden = true
        } else {
            self.lblOptionOther.text = NSLocalizedString("Other", comment: "")
            self.viewOther.isHidden = false
        }
        
        if self.questionOption.isSelected {
            self.select()
        }
        
    }
    
    func select() {
        self.optionSelected = false
        self.viewOption.backgroundColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        if self.questionOption.title! != "OTHER" {
            self.questionOption.isSelected = true
            DiaryHandler.shared.question = self.question
            DiaryHandler.shared.questionOptionList.add(self.questionOption)
            DiaryHandler.shared.answer = self.answer
            print("QuestionCount: \(DiaryHandler.shared.questionOptionList.count)")
        }
    }
    
    func deselect() {
        self.optionSelected = true
        self.viewOption.backgroundColor = UIColor.white
        if questionOption.title! == "OTHER" {
            self.txtAnswer.text = ""
            DiaryHandler.shared.questionOption = nil
            DiaryHandler.shared.question = nil
        } else {
            if DiaryHandler.shared.questionOptionList.count > 0 {
                self.questionOption.isSelected = false
                let index = DiaryHandler.shared.questionOptionList.index(of: self.questionOption)
                DiaryHandler.shared.questionOptionList.removeObject(at: index)
            }
        }
    }
    
    @IBAction func txtAnswerEditingChanged(_ sender: Any) {
        self.questionOption.answer = self.txtAnswer.text!
        print("Question:\(self.question)")
        DiaryHandler.shared.question = self.question
        DiaryHandler.shared.questionOption = self.questionOption
    }

    @IBAction func txtAnswerEditingDidBegin(_ sender: Any) {
        self.delegate?.questionTypeSelectMoreTextFieldTapped(indexPath: self.indexPath)
    }
    
    @IBAction func txtAnswerEditingDidEnd(_ sender: Any) {

    }
}
