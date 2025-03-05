//
//  DBRoleHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBRoleHandler()

class DBRoleHandler: NSObject {
    class var shared: DBRoleHandler {
        return instance
    }
    
    func getRoleOfUser(userId:String) -> DBRole? {
        let results: SRKResultSet = DBRole.query().where(withFormat: "userId = %@", withParameters: [userId]).fetch()
        
        if results.count > 0 {
            let result = results[0]
            let dbRole = result as! DBRole
            return dbRole
        } else {
            return nil
        }
    }
    
    func deleteRolesOfUser(userId:String) {
        let results: SRKResultSet = DBRole.query().where(withFormat: "userId = %@", withParameters: [userId]).fetch()
        for case let role as DBRole in results {
            role.remove()
        }
    }
    
    func deleteAllRoles() {
        DBRole.query().fetch().remove()
    }
}
