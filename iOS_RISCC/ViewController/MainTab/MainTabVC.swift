//
//  MainTabVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class MainTabVC: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
//        self.tabBar.backgroundColor = Util.hexStringToUIColor(hex: COLOR_BASE)
//        self.tabBar.barTintColor = Util.hexStringToUIColor(hex: COLOR_BASE)
        
        let selectedColor   = UIColor.init(hexString: Constants.Color.COLOR_BASE)
        let unselectedColor = #colorLiteral(red: 0.501960814, green: 0.501960814, blue: 0.501960814, alpha: 1)
        
        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedString.Key.foregroundColor: unselectedColor], for: .normal)
        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedString.Key.foregroundColor: selectedColor], for: .selected)
        UITabBarItem.appearance()
        
        tabBar.unselectedItemTintColor = unselectedColor
        tabBar.selectedImageTintColor = selectedColor
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
}
