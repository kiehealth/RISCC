//
//  DiaryUIButton.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class RISCCUIButton: UIButton {
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = UIColor.init(hexString: Constants.Color.COLOR_BASE)
    }
}
