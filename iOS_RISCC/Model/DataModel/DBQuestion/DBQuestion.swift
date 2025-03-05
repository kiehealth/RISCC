//
//  DBQuestion.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBQuestion: SRKObject {
    
    @objc dynamic var questionId: String?
    @objc dynamic var researchId: String?
    @objc dynamic var title: String?
    @objc dynamic var body: String?
    @objc dynamic var displayOrder: Int64 = 0
    @objc dynamic var questionTypeId: String?
    @objc dynamic var questionnaireId: String?
    
    var answer: String = ""
    var questionOptions = NSMutableArray.init()
    var questionType = DBQuestionType.init()
    var question: DBQuestionnaire? = nil
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["questionId": "", "researchId" : "", "title" : "", "body" : "", "displayOrder" : 0, "questionTypeId" : "", "questionnaireId" : ""]
    }
    
    override init() {
        super.init()
        self.questionId = ""
        self.title = ""
        self.body = ""
    }
    
    func parseQuestion(data:JSON) {
        self.questionId = data["id"].stringValue
        self.title = data["title"].stringValue
        self.body = data["body"].stringValue

        let questionTypeData = data["questionType"]
        let dbQuestionType = DBQuestionType.init()
        dbQuestionType.parseQuestionType(data: questionTypeData)
        self.questionTypeId = dbQuestionType.questionTypeId
        
        let questionnaireData = data["questionQuestionnaires"]
        let dbQuestionnaire = DBQuestionnaire.init()
        dbQuestionnaire.parseQuestionnaire(data:questionnaireData)
        self.questionnaireId = dbQuestionnaire.questionQuestionnaireId
        self.displayOrder = dbQuestionnaire.displayOrder
//        print("DisplayOrder: \(self.displayOrder)")
        
        
        let questionOptionsData = data["questionOptions"]
        for questionOptionDict in questionOptionsData.arrayValue {
            if questionOptionDict["id"].exists() {
                let dbQuestionOption = DBQuestionOption.init()
                dbQuestionOption.parseQuestionOption(data: questionOptionDict, questionId: self.questionId!)
            }
        }
    }
}
