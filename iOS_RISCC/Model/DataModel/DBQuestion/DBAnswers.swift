//
//  DBAnswers.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

class DBAnswers: SRKObject {
    
    @objc dynamic var questionId: String?
    @objc dynamic var answer: [String:Any]? = nil
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["questionId" : "", "answer" : [String:Any].self]
    }
    
    override init() {
        super.init()
    }
}
