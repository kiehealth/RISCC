//
//  DBUser.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBUser: SRKObject {

    @objc dynamic var userId: String?
    @objc dynamic var userName: String?
    @objc dynamic var password: String?
    @objc dynamic var token: String?
    
    
    @objc dynamic var firstName: String?
    @objc dynamic var lastName: String?
    @objc dynamic var mobileNumber: String?
    @objc dynamic var email: String?
    @objc dynamic var dateOfBirth: NSDate?
    @objc dynamic var gender: String?
    @objc dynamic var imageUrl: String?
    @objc dynamic var newPassword: String?

    override class func defaultValuesForEntity() -> [String: Any] {
        return ["userId": "", "userName" : "", "password" : "", "token" : "", "chatId" : "", "chatPassword" : "", "firstName" : "", "lastName" : "", "mobileNumber" : "", "email" : "", "dateOfBirth" : NSDate(), "gender" : "", "imageUrl" : "", "newPassword" : ""]
    }
    
    override init() {
        super.init()
        self.userId = ""
        self.userName = ""
        self.password = ""
        self.token = ""
        
        self.token = ""
        self.firstName = ""
        self.lastName = ""
        self.mobileNumber = ""
        self.email = ""
        self.dateOfBirth = NSDate()
        self.gender = ""
    }
    
    func parseUser(user: JSON) {
        self.userId = user["id"].stringValue
        self.firstName = user["firstName"].stringValue
        self.lastName = user["lastName"].stringValue
        self.mobileNumber = user["mobileNumber"].stringValue
        self.email = user["emailAddress"].stringValue

        
        
        
        if user["imageUrl"].exists() {
            self.imageUrl = user["imageUrl"].stringValue
        }
        
        if user["dateOfBirth"].exists() {
           self.dateOfBirth = Util.dateFromUnixTime(unixTimeInterval: user["dateOfBirth"].doubleValue)
        }
        
        if user["gender"].exists() {
            self.gender = user["gender"].stringValue
        }
        
        let roleData = user["role"];
        DBRoleHandler.shared.deleteRolesOfUser(userId: self.userId!);
        let dbRole = DBRole.init()
        dbRole.parseRole(data: roleData, userId: self.userId!)
        dbRole.commit()

    }
}
