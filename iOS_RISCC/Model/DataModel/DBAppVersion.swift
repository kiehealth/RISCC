//
//  DBAppVersion.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBAppVersion: SRKObject {

    @objc dynamic var versionId: String?
    @objc dynamic var version: Double = 0.0
    @objc dynamic var forceUpdate: Bool = false
    @objc dynamic var url: String?
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["versionId" : "", "version" : 0.0, "forceUpdate" : false, "url" : ""]
    }
    
    override init() {
        super.init()
        self.versionId = ""
        self.url = ""
    }
    
    func loadDataFromServer(appVersion:JSON) {
        self.versionId = appVersion["id"].stringValue
        self.version = appVersion["version"].doubleValue
        self.forceUpdate = appVersion["forceUpdate"].boolValue
        self.url = appVersion["url"].stringValue
    }
}
