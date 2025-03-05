//
//  SideMenu.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class SideMenu: NSObject {
    
    var image: String = ""
    var title: String = ""
    var viewController: String = ""
    
    override init() {
        super.init()
    }
    
    init(image:String, title:String, viewController:String) {
        self.image = image
        self.title = title
        self.viewController = viewController
    }

}
