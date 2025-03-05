//
//  DBRole.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBRole: SRKObject {
    @objc dynamic var userId: String?
    @objc dynamic var roleId: String?
    @objc dynamic var title: String?

    override class func defaultValuesForEntity() -> [String: Any] {
        return ["userId": "", "roleId" : "", "title" : ""]
    }
    
    override init() {
        super.init()
        self.userId = ""
        self.roleId = ""
        self.title = ""
    }
    
    func parseRole(data: JSON, userId: String) {
        self.userId = userId
        self.roleId = data["id"].stringValue
        self.title = data["title"].stringValue
        
        let authoritiesData = data["authorities"]
        DBAuthorityHandler.shared.deleteAuthoritiesOfRole(roleId: self.roleId!);
        for authorityDict in authoritiesData.arrayValue {
            let dbAuthority = DBAuthority.init()
            dbAuthority.parseAuthority(data: authorityDict, roleId: self.roleId!)
            dbAuthority.commit()
        }
    }
}
