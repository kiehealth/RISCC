//
//  DBAuthority.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAuthority: SRKObject {
    @objc dynamic var authorityId: String?
    @objc dynamic var title: String?
    @objc dynamic var roleId: String?

    override class func defaultValuesForEntity() -> [String: Any] {
        return ["authorityId": "", "title" : "", "roleId" : ""]
    }
    
    override init() {
        super.init()
        self.authorityId = ""
        self.title = ""
        self.roleId = ""
    }
    
    func parseAuthority(data: JSON, roleId: String) {
        self.roleId = roleId
        self.authorityId = data["id"].stringValue
        self.title = data["title"].stringValue
    }
}
