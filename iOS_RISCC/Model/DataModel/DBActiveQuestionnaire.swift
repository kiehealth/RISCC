//
//  DBActiveQuestionnaire.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBActiveQuestionnaire: SRKObject {

    @objc dynamic var questionId: Int = 0
    @objc dynamic var questionnaireId: Int = 0
    @objc dynamic var groupId: Int = 0
    @objc dynamic var title: String?
    @objc dynamic var reminderTimeInterval: Int = 0
    @objc dynamic var reminderMessage: String?
    @objc dynamic var startedDate: Int64 = 0
    @objc dynamic var endDateTime: Int64 = 0
    
    override init() {
        super.init()
    }
    
    func loadDataFromServer(data:JSON) {
        self.questionId = data["id"].intValue
        self.reminderTimeInterval = data["reminderTimeInterval"].intValue
        self.reminderMessage = data["reminderMessage"].stringValue
        self.groupId = data["id"].intValue
        self.startedDate = data["startDateTime"].int64Value
        self.endDateTime = data["endDateTime"].int64Value
        let questionnaireData = data["questionnaire"]
        self.questionnaireId = questionnaireData["id"].intValue
        self.title = questionnaireData["title"].stringValue
    }
}
