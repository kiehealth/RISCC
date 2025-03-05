//
//  DBInfo.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBInfo: SRKObject {

    @objc dynamic var infoId: String?
    @objc dynamic var title: String?
    @objc dynamic var desc: String?
    @objc dynamic var url: String?
    @objc dynamic var emailAddress: String?
    @objc dynamic var contactNumber: String?
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["infoId": "", "title" : "", "desc" : "", "url" : "", "emailAddress" : "", "contactNumber" : ""]
    }
    
    override init() {
        super.init()
        self.infoId = ""
        self.title = ""
        self.desc = ""
        self.url = ""
        self.emailAddress = ""
        self.contactNumber = ""
    }
    
    func loadDataFromServer(data:JSON) {
        self.infoId = data["id"].stringValue
        self.title = data["title"].stringValue
        self.desc = data["description"].stringValue
        self.url = data["url"].stringValue.trimmingCharacters(in: .whitespaces)
        self.emailAddress = data["emailAddress"].stringValue
        self.contactNumber = data["contactNumber"].stringValue
    }
}
