//
//  DBAboutUsHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBAboutUsHandler()

class DBAboutUsHandler: NSObject {
    
    class var shared: DBAboutUsHandler {
        return instance
    }
    
    func getAboutUs() -> DBAboutUs? {
        let results = DBAboutUs.query().fetch()
        if results.count > 0 {
            let aboutUs = results[0] as! DBAboutUs
            return aboutUs
        } else {
            return nil
        }
    }
    
    func deleteAll() {
        DBAboutUs.query().fetch().remove()
    }

}
