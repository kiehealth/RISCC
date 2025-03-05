//
//  CellAnsweredQuestion.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellAnsweredQuestion: UITableViewCell {

    @IBOutlet weak var lblQuestion: UILabel!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblAnswer: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }
    
    func load(answer: DBAnswerData) {
        self.lblQuestion.text = answer.question.title
        self.lblDesc.text = answer.question.body
        self.lblAnswer.text = "\(NSLocalizedString("Answer", comment: "")): \((answer.answer != nil ? answer.answer : answer.allAnswer)!)"
    }

}
