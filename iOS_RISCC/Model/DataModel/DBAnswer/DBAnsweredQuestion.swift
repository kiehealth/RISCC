//
//  DBAnsweredQuestion.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAnsweredQuestion: SRKObject {

    @objc dynamic var questionId: Int = 0
    @objc dynamic var title: String?
    @objc dynamic var body: String?
    @objc dynamic var questionType: String?
    
    override init() {
        super.init()
    }
    
    func loadDataFromServer(data:JSON) {
        self.questionId = data["id"].intValue
        self.title = data["title"].stringValue
        self.body = data["body"].stringValue
        
        let questionTypeJSON = data["questionType"]
        self.questionType = questionTypeJSON["title"].stringValue
    }
}
