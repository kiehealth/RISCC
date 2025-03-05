//
//  DBPreference.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

class DBPreference: SRKObject {

    @objc dynamic var isUnreadMessageNotificationEnabled: Bool = true
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["isUnreadMessageNotificationEnabled" : false]
    }
}
