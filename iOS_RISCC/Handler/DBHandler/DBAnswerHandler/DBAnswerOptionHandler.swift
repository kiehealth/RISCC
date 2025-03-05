//
//  DBAnswerOptionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAnswerOptionHandler()

class DBAnswerOptionHandler: NSObject {

    class var shared: DBAnswerOptionHandler {
        return instance
    }
    
    func getAnswerOptionInString(answerId:Int) -> String {
        let result: SRKResultSet = DBAnswerOption.query().where(withFormat: "answerId = %@", withParameters: [answerId]).fetch()
        var answerStringList = NSMutableArray.init() as! [String]
        let answerList = NSMutableArray.init()
        for case let dbAnswer as DBAnswerOption in result {
            answerStringList.append((dbAnswer.title)!)
            answerList.add(dbAnswer)
        }
        let answerString = answerStringList.compactMap{ $0 }.joined(separator: ", ")
        return answerString
    }
    
    func deleteAllAnswerOption() {
        DBAnswerOption.query().fetch().remove()
    }
}
