//
//  DBAnswerDataHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAnswerDataHandler()

class DBAnswerDataHandler: NSObject {

    class var shared: DBAnswerDataHandler {
        return instance
    }
    
    func getAnswers() -> NSMutableArray {
        let results: SRKResultSet = DBAnswerData.query().fetch()
        let answers = NSMutableArray()
        for case let answer as DBAnswerData in results {
            answer.allAnswer = DBAnswerOptionHandler.shared.getAnswerOptionInString(answerId: answer.answerId)
            answer.question = DBAnsweredQuestionHandler.shared.getQuestion(questionId: answer.questionId)
            answers.add(answer)
        }
        return answers
    }
    
    func deleteAllAnswerData() {
        DBAnswerData.query().fetch().remove()
    }
}
