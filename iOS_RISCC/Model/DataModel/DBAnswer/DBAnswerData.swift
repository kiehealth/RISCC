//
//  DBAnswerData.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAnswerData: SRKObject {

    @objc dynamic var answerId: Int = 0
    @objc dynamic var dateTime: Int64 = 0
    @objc dynamic var answer: String?
    @objc dynamic var questionId: Int = 0
    
    var answerOption = DBAnswerOption.init()
    var question = DBAnsweredQuestion.init()
    var allAnswer: String = ""
    
    override init() {
        super.init()
    }
    
    func loadDataFromServer(data:JSON) {
        self.answerId = data["id"].intValue
        self.dateTime = data["dateTime"].int64Value
        if data["answer"].exists() {
            self.answer = data["answer"].stringValue
        } else {
            let answerOptionJSON = data["answerOptions"]
            for answerOptionDict in answerOptionJSON.arrayValue {
                let answerOption = DBAnswerOption.init()
                answerOption.answerId = self.answerId
                answerOption.loadDataFromServer(data: answerOptionDict)
                answerOption.commit()
            }
        }
        
        let questionJSON = data["question"]
        let answeredQuestion = DBAnsweredQuestion.init()
        answeredQuestion.loadDataFromServer(data: questionJSON)
        answeredQuestion.commit()
        self.questionId = answeredQuestion.questionId
    }
}
