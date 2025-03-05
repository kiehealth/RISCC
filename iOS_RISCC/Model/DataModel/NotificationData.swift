//
//  NotificationData.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SwiftyJSON

class NotificationData: NSObject {
    
    var userId: String? = ""
    var groupId: String? = ""
    var type: String? = ""
    
    func loadNotifcaiton(data:JSON){
        print("Notification data: \(data)")
        if data["userId"].exists() {
            self.userId = data["userId"].stringValue
        } else {
            self.userId = ""
        }
        
        if data["groupId"].exists() {
            self.groupId = data["groupId"].stringValue
        } else {
            self.groupId = ""
        }
        
        if data["type"].exists() {
            self.type = data["type"].stringValue
        } else {
            self.type = ""
        }
        
    }

}
