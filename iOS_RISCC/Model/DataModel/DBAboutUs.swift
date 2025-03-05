//
//  DBAboutUs.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAboutUs: SRKObject {
    
    @objc dynamic var aboutUsId: String? = ""
    @objc dynamic var name: String? = ""
    @objc dynamic var phone: String? = ""
    @objc dynamic var email: String? = ""
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["aboutUsId": "", "name" : "", "phone" : "", "email" : ""]
    }
    
    override init() {
        super.init()
    }
    
    func loadDataFromServer(data:JSON) {
        self.aboutUsId = data["id"].stringValue
        self.name = data["name"].stringValue
        self.phone = data["phone"].stringValue
        self.email = data["email"].stringValue
    }
}
