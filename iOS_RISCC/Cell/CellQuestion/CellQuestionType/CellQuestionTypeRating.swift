//
//  CellQuestionTypeRating.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellQuestionTypeRating: UITableViewCell {

    @IBOutlet weak var viewOption: UIView!
    @IBOutlet weak var lblOption: UILabel!
    
    var question: DBQuestion = DBQuestion.init()
    var questionOption = DBQuestionOption.init()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func loadQuestionOption(questionOption:DBQuestionOption, question:DBQuestion) {
        self.questionOption = questionOption
        self.question = question
        self.lblOption.text = questionOption.title!
        
        if self.questionOption.isSelected {
            self.select()
        }
    }
    
    func select() {
        self.viewOption.backgroundColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        DiaryHandler.shared.question = self.question
        DiaryHandler.shared.questionOption = self.questionOption
    }
    
    func deselect() {
        self.viewOption.backgroundColor = UIColor.white
    }
    
}
