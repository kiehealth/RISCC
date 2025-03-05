//
//  DBAnalytics.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAnalytics: SRKObject {
    
    @objc dynamic var analyticId: String? = ""
    @objc dynamic var title: String? = ""
    @objc dynamic var desc: String? = ""
    @objc dynamic var appAnalyticsDataType: String? = ""
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["analyticId" : "", "title" : "", "desc" : "", "appAnalyticsDataType" : ""]
    }
    
    func loadDataFromServer(data:JSON) {
        self.analyticId = data["id"].stringValue
        self.title = data["title"].stringValue
        self.desc = data["description"].stringValue
        self.appAnalyticsDataType = data["appAnalyticsDataType"].stringValue
    }
}
