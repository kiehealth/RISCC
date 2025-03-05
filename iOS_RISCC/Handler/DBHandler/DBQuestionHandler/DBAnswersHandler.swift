//
//  DBAnswersHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAnswersHandler()

class DBAnswersHandler: NSObject {

    class var shared: DBAnswersHandler {
        return instance
    }
    
    func getAnswers() -> NSMutableArray {
        let results: SRKResultSet = DBAnswers.query().fetch()
        let answers = NSMutableArray.init()
        for case let answer as DBAnswers in results {
            answers.add(answer)
        }
        return answers
    }
    
    func answerBy(questionId:String) -> DBAnswers {
        let results: SRKResultSet = DBAnswers.query().where(withFormat: "questionId  = %@", withParameters: [questionId]).fetch()
        if results.count > 0 {
            return results[0] as! DBAnswers
        } else {
            return DBAnswers.init()
        }
    }
    
    func deleteAllAnswers() {
        DBAnswers.query().fetch().remove()
    }
}
