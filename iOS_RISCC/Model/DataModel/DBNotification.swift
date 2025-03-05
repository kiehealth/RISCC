//
//  DBNotification.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBNotification: SRKObject {

    @objc dynamic var notificationId: String? = ""
    @objc dynamic var title: String? = ""
    @objc dynamic var desc: String? = ""
    @objc dynamic var notificationType: String? = ""
    @objc dynamic var date: NSDate? = NSDate()
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["notificationId": "", "title" : "", "desc": "", "name" : "", "date": NSDate()]
    }
    
    override init() {
        super.init()
    }
    
    func loadDataFromServer(data:JSON) {
        self.notificationId = data["id"].stringValue
        self.title = data["title"].stringValue
        self.desc = data["description"].stringValue
        self.notificationType = data["notificationType"].stringValue
        self.date = Util.dateFromUnixTime(unixTimeInterval: data["dateTime"].doubleValue)
    }
}
