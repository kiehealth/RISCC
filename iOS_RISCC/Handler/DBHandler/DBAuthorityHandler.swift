//
//  DBAuthorityHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAuthorityHandler()

class DBAuthorityHandler: NSObject {
    
    class var shared: DBAuthorityHandler {
        return instance
    }
    
    func getAuthoritiesOfRole(roleId:String) -> NSMutableArray {
        let results: SRKResultSet = DBAuthority.query().where(withFormat: "roleId = %@", withParameters: [roleId]).fetch()
        let authorityList = NSMutableArray.init()
        for case let authority as DBAuthority in results {
            authorityList.add(authority)
        }
        return authorityList
    }
    
    func deleteAuthoritiesOfRole(roleId:String) {
        let results: SRKResultSet = DBAuthority.query().where(withFormat: "roleId = %@", withParameters: [roleId]).fetch()
        for case let authority as DBAuthority in results {
            authority.remove()
        }

    }
    
    func deleteAllAuthorities() {
        DBAuthority.query().fetch().remove()
    }
}
