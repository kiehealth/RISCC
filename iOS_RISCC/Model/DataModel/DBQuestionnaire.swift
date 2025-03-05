//
//  DBQuestionnaire.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBQuestionnaire: SRKObject {
    
    @objc dynamic var questionQuestionnaireId: String?
    @objc dynamic var questionnaireId: String?
    @objc dynamic var title: String?
    @objc dynamic var active: Bool = false
    @objc dynamic var displayOrder: Int64 = 0

    override class func defaultValuesForEntity() -> [String: Any] {
        return ["questionQuestionnaireId" : "","questionnaireId": "", "title" : "", "active" : false, "displayOrder" : 0]
    }
    
    override init() {
        super.init()
        self.questionnaireId = ""
        self.title = ""
    }

    func parseQuestionnaire(data: JSON) {
        for questionnaireDict in data.arrayValue {
            let questionnaire = questionnaireDict["questionnaire"]
//            if questionnaire["active"].boolValue {
                self.questionQuestionnaireId = questionnaireDict["id"].stringValue
                self.questionnaireId = questionnaire["id"].stringValue
                self.title = questionnaire["title"].stringValue
//                self.active = questionnaire["active"].boolValue
                self.displayOrder = questionnaireDict["displayOrder"].int64Value
//                let dbQuestionnaire = DBQuestionnaireHandler.shared.getQuestionnaire(questionnaireId: self.questionnaireId!);
//                if dbQuestionnaire == nil {
//                    commit()
//                } else {
//                    dbQuestionnaire?.title = self.title
//                    dbQuestionnaire?.active = self.active
//                    dbQuestionnaire?.displayOrder = self.displayOrder
//                    dbQuestionnaire?.commit()
//                }
//            }
        }
    }
}
