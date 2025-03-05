//
//  DBSession.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import UIKit
import SharkORM

class DBSession: SRKObject {
    @objc dynamic var inDate: NSDate?
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["inDate": NSDate.init()]
    }
    
    override init() {
        super.init()
        self.inDate = NSDate()
    }
    
    override var description: String {
        return ("InDate: \(String(describing: self.inDate))")
    }
}
