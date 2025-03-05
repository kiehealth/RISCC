//
//  CellDataInput.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellQuestionTypeDataInput: UITableViewCell {

    @IBOutlet weak var txtAnswer: UITextField!
    
    var question = DBQuestion.init()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.txtAnswer.placeholder = NSLocalizedString("Your Answer", comment: "")
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func load(question:DBQuestion) {
        self.question = question
        self.txtAnswer.text = self.question.answer
        
    }

    @IBAction func txtAnswerEditingChanged(_ sender: Any) {
        self.question.answer = self.txtAnswer.text!
        DiaryHandler.shared.question = self.question
        let answer = AnswerModel.init()
        answer.answer = self.txtAnswer.text!
        answer.question = self.question
        DiaryHandler.shared.answer = answer
    }
    
}
