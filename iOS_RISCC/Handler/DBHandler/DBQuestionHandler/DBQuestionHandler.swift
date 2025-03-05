//
//  DBQuestionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBQuestionHandler()

class DBQuestionHandler: NSObject {

    class var shared: DBQuestionHandler {
        return instance
    }
    
    func getAllQuestions(success:@escaping((_ questions:NSMutableArray)->Void), failure:@escaping((_ message:String)->Void)) {
        do {
            let results: SRKResultSet = DBQuestion.query().fetch()
            let questions = NSMutableArray.init()
            let noneTypeQuestionCount: Int = DBQuestionTypeHandler.shared.getQuestionTypeNoneCount()
            let countExceptNone: Int = DBQuestionTypeHandler.shared.getCountExceptNoneType()
            for case let question as DBQuestion in results {
                question.questionOptions = DBQuestionOptionHandler.shared.getQuestionOptions(questionId: question.questionId!)
                question.questionType = DBQuestionTypeHandler.shared.getQuestionType(questionTypeId: question.questionTypeId!)!
                
                if question.questionType.title == QUESTION_TYPE.None.rawValue && countExceptNone == 0 {
                    if noneTypeQuestionCount > 1 {
                        questions.add(question)
                    }
                } else {
                    questions.add(question)
                }
            }
            let descriptor = NSSortDescriptor(key: "displayOrder", ascending: true)
            questions.sort(using: [descriptor])
            success(questions)
        } catch {
            print("No question available.")
            failure("No question available.")
        }
    }
    
    func deleteAllQuestions() {
        DBQuestion.query().fetch().remove()
    }
    
}
