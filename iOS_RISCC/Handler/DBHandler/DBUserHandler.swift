//
//  DBUserHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBUserHandler()

class DBUserHandler: NSObject {

    class var shared: DBUserHandler {
        return instance
    }
    
    func getLoggedUser()-> DBUser? {
        
        let results : SRKResultSet = DBUser.query().fetch()
        if results.count > 0 {
            let result = results[0]
            let user = result as! DBUser
            return user
        } else {
            return nil
        }
    }
    
    func getUser(userId:String, success:@escaping((_ dbUser:DBUser)->Void), failure:@escaping((_ message:String)->Void)){
        do {
            let results : SRKResultSet = DBUser.query().where(withFormat: "userId = %@", withParameters: [userId]).fetch()
            if results.count > 0 {
                let result = results[0]
                let dbUser = result as! DBUser
                success(dbUser)
            } else {
                failure("No user exists")
            }
        } catch {
            failure("No user exists")
        }
    }
    
    func deleteAllUser() {
        DBUser.query().fetch().remove()
    }
}
