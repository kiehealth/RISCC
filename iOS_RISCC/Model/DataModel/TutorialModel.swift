//
//  TutorialModel.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class TutorialModel: NSObject {
    
    var title: String = ""
    var image: String = ""
    var desc: String = ""
    
    override init() {
        super.init()
    }
    
    init(title:String, image:String, desc:String) {
        self.title = title
        self.image = image
        self.desc = desc
    }

}
