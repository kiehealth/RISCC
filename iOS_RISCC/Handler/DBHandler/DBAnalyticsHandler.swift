//
//  DBAnalyticsHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAnalyticsHandler()

class DBAnalyticsHandler: NSObject {
    
    class var shared: DBAnalyticsHandler {
        return instance
    }
    
    func getAnalyticsTypeWith(title:String) -> DBAnalytics? {
        let results: SRKResultSet = DBAnalytics.query().where(withFormat: "title = %@", withParameters: [title]).fetch()
        if results.count > 0 {
            return results[0] as! DBAnalytics
        } else {
            return nil
        }
    }
    
    func deleteAllAnalytics() {
        DBAnalytics.query().fetch().remove()
    }
}
