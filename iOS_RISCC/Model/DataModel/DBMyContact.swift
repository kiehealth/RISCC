//
//  DBMyContact.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBMyContact: SRKObject {

    @objc dynamic var contactId: String? = ""
    @objc dynamic var service:String? = ""
    @objc dynamic var name: String? = ""
    @objc dynamic var phoneNumber: String? = ""
    @objc dynamic var email: String? = ""
    @objc dynamic var date: NSDate? = NSDate()
    @objc dynamic var isSync: Bool = false
    @objc dynamic var unsyncedUpdate: Bool = false
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["contactId": "", "service" : "", "name" : "", "phoneNumber" : "", "email" : "", "date" : NSDate(), "isSync" : false, "unsyncedUpdate" : false]
    }
    
    override init() {
        super.init()
    }
    
    func saveData(contactId:String, service:String, name: String, phoneNumber:String, email:String, isSync: Bool, unsyncedUpdate:Bool) {
        self.contactId = contactId
        self.service = service
        self.name = name
        self.phoneNumber = phoneNumber
        self.email = email
        self.isSync = isSync
        self.unsyncedUpdate = unsyncedUpdate
    }
    
    func loadDataFromServer(data:JSON) {
        self.contactId = data["id"].stringValue
        self.service = data["service"].stringValue
        self.name = data["name"].stringValue
        self.phoneNumber = data["phoneNumber"].stringValue
        self.email = data["email"].stringValue
        self.date = Util.dateFromUnixTime(unixTimeInterval: data["dateTime"].doubleValue)
        self.isSync = true
        self.unsyncedUpdate = false
    }
}
