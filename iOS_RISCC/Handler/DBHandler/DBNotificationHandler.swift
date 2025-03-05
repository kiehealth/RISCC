//
//  DBNotificationHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBNotificationHandler()

class DBNotificationHandler: NSObject {
    
    class var shared: DBNotificationHandler {
        return instance
    }
    
    func getNotifications(success:@escaping((_ notifications:NSMutableArray)->Void), failure:@escaping((_ message:String)->Void)) {
        do {
            let results: SRKResultSet = DBNotification.query().fetch()
            let notifications = NSMutableArray.init()
            for case let notification as DBNotification in results {
                notifications.add(notification)
            }
            let sortDescriptor = NSSortDescriptor(key: "date", ascending: false)
            notifications.sort(using: [sortDescriptor])
            success(notifications)
        } catch {
            failure("No notification exist.")
        }
    }
    
    func deleteAllNotifications() {
        DBNotification.query().fetch().remove()
    }

}
