//
//  DBAnswerOption.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAnswerOption: SRKObject {
    
    @objc dynamic var answerId: Int = 0
    @objc dynamic var optionId: Int = 0
    @objc dynamic var title: String?
    @objc dynamic var value: String?
    
    override init() {
        super.init()
    }
    
    func loadDataFromServer(data:JSON) {
        self.optionId = data["id"].intValue
        self.title = data["title"].stringValue
        self.value = data["value"].stringValue
    }
}
