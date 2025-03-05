//
//  DBQuestionType.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBQuestionType: SRKObject {
    @objc dynamic var questionTypeId: String?
    @objc dynamic var title: String?

    override class func defaultValuesForEntity() -> [String: Any] {
        return ["questionTypeId": "", "title" : ""]
    }
    
    override init() {
        super.init()
        self.questionTypeId = ""
        self.title = ""
    }
    
    func parseQuestionType(data: JSON) {
        self.questionTypeId = data["id"].stringValue
        self.title = data["title"].stringValue
        let dbQuestionType = DBQuestionTypeHandler.shared.getQuestionType(questionTypeId: self.questionTypeId!);
        if dbQuestionType == nil {
            commit()
        } else {
            dbQuestionType?.title = self.title
            dbQuestionType?.commit()
        }
    }
}
