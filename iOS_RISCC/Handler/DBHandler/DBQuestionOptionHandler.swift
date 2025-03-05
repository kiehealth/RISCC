//
//  DBQuestionOptionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBQuestionOptionHandler()

class DBQuestionOptionHandler: NSObject {
    
    class var shared: DBQuestionOptionHandler {
        return instance
    }
    
    func getQuestionOptions(questionId:String) -> NSMutableArray {
        let results: SRKResultSet = DBQuestionOption.query().where(withFormat: "questionId = %@", withParameters: [questionId]).fetch()
        let questionOptions = NSMutableArray.init()
        for case let questionOption as DBQuestionOption in results {
            questionOptions.add(questionOption)
        }
        let firstDescriptor = NSSortDescriptor(key: "value", ascending: true)
        let secondDescriptor = NSSortDescriptor(key: "title", ascending: true)
        questionOptions.sort(using: [firstDescriptor, secondDescriptor])
        return questionOptions
    }
    
    func getQuestionOption(questionId:String, questionOptionId:String) -> DBQuestionOption? {
        let results: SRKResultSet = DBQuestionOption.query().where(withFormat: "questionId = %@ AND questionOptionId = %@", withParameters: [questionId, questionOptionId]).fetch()
        
        if results.count > 0 {
            let result = results[0]
            let dbQuestionOption = result as! DBQuestionOption
            return dbQuestionOption
        } else {
            return nil
        }
        
    }
    
    func deleteAllQuestionOption() {
        DBQuestionOption.query().fetch().remove()
    }

}
