//
//  DBAppVersionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAppVersionHandler()

class DBAppVersionHandler: NSObject {
    
    class var shared: DBAppVersionHandler {
        return instance
    }
    
    func getAppVersion() -> DBAppVersion {
        let results = DBAppVersion.query().fetch()
        if results.count > 0 {
            let appVersion = results[0] as! DBAppVersion
            return appVersion
        } else {
            return DBAppVersion()
        }
    }
    
    func deleteAppVersion() {
        DBAppVersion.query().fetch().remove()
    }
}
