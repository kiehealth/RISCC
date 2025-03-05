//
//  CellAnsweredQuestionTypeDataInput.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellAnsweredQuestionTypeDataInput: UITableViewCell {

    @IBOutlet weak var txtAnswer: UITextField!
    
    var answeredQuestion = DBAnsweredQuestion.init()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    func load(answeredQuestion:DBAnsweredQuestion) {
        self.answeredQuestion = answeredQuestion
//        self.txtAnswer.text = self.answeredQuestion.answer
        
    }
    
    @IBAction func txtAnswerEditingChanged(_ sender: Any) {
//        self.answeredQuestion.answer = self.txtAnswer.text!
        DiaryHandler.shared.answeredQuestion = self.answeredQuestion
        
    }

}
