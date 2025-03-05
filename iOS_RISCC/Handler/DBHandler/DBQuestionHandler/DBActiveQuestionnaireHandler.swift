//
//  DBActiveQuestionnaireHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBActiveQuestionnaireHandler()

class DBActiveQuestionnaireHandler: NSObject {

    class var shared: DBActiveQuestionnaireHandler {
        return instance
    }
    
    func getActiveQuestionnaireList() -> NSMutableArray {
        let results: SRKResultSet = DBActiveQuestionnaire.query().fetch()
        let questionnaires = NSMutableArray.init()
        for case let questionnaire as DBActiveQuestionnaire in results {
            questionnaires.add(questionnaire)
        }
        return questionnaires
    }
    
    func deleteAllActiveQuestionnaires() {
        DBActiveQuestionnaire.query().fetch().remove()
    }
}
