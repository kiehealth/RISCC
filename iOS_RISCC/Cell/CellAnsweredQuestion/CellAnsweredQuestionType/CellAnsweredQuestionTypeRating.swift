//
//  CellAnsweredQuestionTypeRating.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellAnsweredQuestionTypeRating: UITableViewCell {

    @IBOutlet weak var viewOption: UIView!
    @IBOutlet weak var lblOption: UILabel!
    
    var answeredQuestion: DBAnsweredQuestion = DBAnsweredQuestion.init()
//    var answeredQuestionOption = DBAnsweredQuestionOption.init()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
//    func loadQuestionOption(answeredQuestionOption:DBAnsweredQuestionOption, answeredQuestion:DBAnsweredQuestion) {
//        self.answeredQuestionOption = answeredQuestionOption
//        self.answeredQuestion = answeredQuestion
//        self.lblOption.text = answeredQuestionOption.title!
//    }
//    
    func select() {
        self.viewOption.backgroundColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)
        DiaryHandler.shared.answeredQuestion = self.answeredQuestion
//        DiaryHandler.shared.answeredQuestionOption = self.answeredQuestionOption
    }
    
    func deselect() {
        self.viewOption.backgroundColor = UIColor.white
    }

}
