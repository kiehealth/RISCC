//
//  DBAnsweredQuestionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAnsweredQuestionHandler()

class DBAnsweredQuestionHandler: NSObject {
    
    class var shared: DBAnsweredQuestionHandler {
        return instance
    }
    
    func getQuestion(questionId:Int) -> DBAnsweredQuestion {
        let results: SRKResultSet = DBAnsweredQuestion.query().where(withFormat: "questionId = %@", withParameters: [questionId]).fetch()
        var question = DBAnsweredQuestion()
        if results.count > 0 {
            question = results[0] as! DBAnsweredQuestion
        }
        return question
    }
    
    func deleteAllAnsweredQuestion() {
        DBAnsweredQuestion.query().fetch().remove()
    }
}
