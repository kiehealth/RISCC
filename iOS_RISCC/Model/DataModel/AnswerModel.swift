//
//  AnswerModel.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class AnswerModel: NSObject {
    
    var answer: String = ""
    var question: DBQuestion? = nil
    var questionOption: DBQuestionOption? = nil
    var questionOptionList = NSMutableArray.init()
    
    var isSelected: Bool = false
    
    override init() {
        super.init()
    }
}
