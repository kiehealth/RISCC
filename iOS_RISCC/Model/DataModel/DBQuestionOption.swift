//
//  DBQuestionOption.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBQuestionOption: SRKObject {
    @objc dynamic var questionOptionId: String?
    @objc dynamic var title: String?
    @objc dynamic var value: Int64 = 0
    @objc dynamic var questionId: String?
    
    var answer: String = ""
    var isSelected: Bool = false

    override class func defaultValuesForEntity() -> [String: Any] {
        return ["questionOptionId": "", "title" : "", "value" : 0, "questionId" : ""]
    }
    
    override init() {
        super.init()
        self.questionOptionId = ""
        self.title = ""
        self.value = 0
        self.questionId = ""
    }
    
    func parseQuestionOption(data: JSON, questionId: String) {
        self.questionId = questionId
        self.questionOptionId = data["id"].stringValue
        
        let dbQuestionOption = DBQuestionOptionHandler.shared.getQuestionOption(questionId: questionId, questionOptionId: self.questionOptionId!)
        if dbQuestionOption != nil {
            dbQuestionOption?.title = data["title"].stringValue;
            dbQuestionOption?.value = Int64(data["value"].intValue)
            dbQuestionOption?.value = dbQuestionOption?.title == QUESTION_TYPE_OTHER ? 100 : 0
            dbQuestionOption?.commit()
        } else {
            self.title = data["title"].stringValue
            self.value = Int64(data["value"].intValue)
//            self.value = self.title == QUESTION_TYPE_OTHER ? 100 : 0
            commit()
        }
    }
}
