//
//  DBPreferenceHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBPreferenceHandler()

class DBPreferenceHandler: NSObject {
    
    class var shared: DBPreferenceHandler {
        return instance
    }
    
    func getPreference() -> DBPreference {
        let results: SRKResultSet = DBPreference.query().fetch()
        if results.count > 0 {
            let preference = results[0] as! DBPreference
            return preference
        } else {
            return DBPreference()
        }
    }
    
    func deleteAllPreferences() {
        DBPreference.query().fetch().remove()
    }

}
