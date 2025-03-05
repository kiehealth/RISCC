//
//  Language.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class Language: NSObject {

    var language: String = ""
    var code: String = ""
    
    init(language:String, code:String) {
        self.language = language
        self.code = code
    }
}
