//
//  DBSessionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBSessionHandler()

class DBSessionHandler: NSObject {
    
    class var shared: DBSessionHandler {
        return instance
    }
    
    func isAppRinningFirstTime()->Bool{
        do {
            let  results : SRKResultSet = DBSession.query().fetch()
            return results.count <= 1
        } catch {
            return true
        }
    }
    
    func deleteAllSession() {
        DBSession.query().fetch().remove()
    }
}
