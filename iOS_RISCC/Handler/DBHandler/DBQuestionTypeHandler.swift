//
//  DBQuestionTypeHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBQuestionTypeHandler()

class DBQuestionTypeHandler: NSObject {
    class var shared: DBQuestionTypeHandler {
        return instance
    }
    
    func getQuestionType(questionTypeId:String) -> DBQuestionType? {
        let results: SRKResultSet = DBQuestionType.query().where(withFormat: "questionTypeId = %@", withParameters: [questionTypeId]).fetch()
        if results.count > 0 {
            let result = results[0]
            let dbQuestionType = result as! DBQuestionType
            return dbQuestionType
        } else {
            return nil
        }
    }
    
    func getQuestionTypeNoneCount() -> Int {
        let results: SRKResultSet = DBQuestionType.query().where(withFormat: "title = %@", withParameters: [QUESTION_TYPE.None.rawValue]).fetch()
        return results.count
    }
    
    func getCountExceptNoneType() -> Int {
        var count: Int = 0
        let array = [QUESTION_TYPE.DataInput.rawValue, QUESTION_TYPE.SelectOne.rawValue, QUESTION_TYPE.SelectMore.rawValue, QUESTION_TYPE.Rating.rawValue]
        array.forEach { (questionType) in
            let results: SRKResultSet = DBQuestionType.query().where(withFormat: "title = %@", withParameters: [questionType]).fetch()
            count += results.count
        }
        return count
    }
    
    func deleteAllQuestionType() {
        DBQuestionType.query().fetch().remove()
    }
    
}
