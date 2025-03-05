//
//  Validation.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class Validation: NSObject {

    var status:Bool
    var message:String
    
    override init() {
        self.status = false
        self.message = ""
    }
    
    init(status:Bool, message:String) {
        self.status = status
        self.message = message
    }
}
