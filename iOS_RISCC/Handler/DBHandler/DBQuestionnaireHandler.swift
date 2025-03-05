//
//  DBQuestionnaireHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBQuestionnaireHandler()

class DBQuestionnaireHandler: NSObject {

    class var shared: DBQuestionnaireHandler {
        return instance
    }
    
    func getQuestionnaire() -> DBQuestionnaire? {
        let results: SRKResultSet = DBQuestionnaire.query().fetch()
        if results.count > 0 {
            let result = results[0]
            let dbQuestionnaire = result as! DBQuestionnaire
            return dbQuestionnaire
        } else {
            return nil
        }
    }
    
//    func getQuestionnaire(questionnaireId:String) -> DBQuestionnaire? {
//        let results: SRKResultSet = DBQuestionnaire.query().where(withFormat: "questionnaireId = %@", withParameters: [questionnaireId]).fetch()
//        if results.count > 0 {
//            let result = results[0]
//            let dbQuestionnaire = result as! DBQuestionnaire
//            return dbQuestionnaire
//        } else {
//            return nil
//        }
//    }
    
    func deleteAllQuestionnaire() {
        DBQuestionnaire.query().fetch().remove()
    }
}
