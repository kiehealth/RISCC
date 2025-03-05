//
//  DBContact.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBContact: SRKObject {

    @objc dynamic var userId: String?
    @objc dynamic var loggedUserId: String?
    @objc dynamic var userName: String?
    @objc dynamic var password: String?
    @objc dynamic var token: String?
    @objc dynamic var chatId: String?
    @objc dynamic var isOnlineInChat: Bool = false
    @objc dynamic var isLoggedUser: Bool = false
    
    @objc dynamic var firstName: String?
    @objc dynamic var lastName: String?
    @objc dynamic var mobileNumber: String?
    @objc dynamic var email: String?
    @objc dynamic var dateOfBirth: NSDate?
    @objc dynamic var gender: String?
    @objc dynamic var imageUrl: String?
    @objc dynamic var groupId: String?
    @objc dynamic var newPassword: String?
    
    var isChecked: Bool = false //Used for selecting/deselecting user in groupchat
    var isOwner: Bool = false
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["userId": "", "loggedUserId" : "", "userName" : "", "password" : "", "token" : "", "chatId" : "", "isOnlineInChat" : false, "firstName" : "", "lastName" : "", "mobileNumber" : "", "email" : "", "dateOfBirth" : NSDate(), "gender" : "", "imageUrl" : "", "groupId" : "", "newPassword" : ""]
    }
    
    override init() {
        super.init()
        self.userId = ""
        self.loggedUserId = ""
        self.userName = ""
        self.password = ""
        self.token = ""
        self.chatId = "none@13.53.73.11"
        self.isOnlineInChat = false
        
        self.token = ""
        self.firstName = ""
        self.lastName = ""
        self.mobileNumber = ""
        self.email = ""
        self.dateOfBirth = NSDate()
        self.gender = ""
        self.groupId = ""
    }
    
    func parseGroupUser(user: JSON) {
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
        
        if user["chatId"].exists() {
            self.chatId = user["chatId"].stringValue
        }
    }
}
