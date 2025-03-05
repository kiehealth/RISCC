//
//  Image+Extended.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 19/03/2021.
//

import Foundation
import UIKit

class ImageColor: UIImageView {
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.image = image?.imageWithColor(color: UIColor.init(hexString: Constants.Color.COLOR_BASE))
    }
}

class ImageBackgroundColor: UIImageView {
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = UIColor.init(hexString: Constants.Color.COLOR_BASE)
    }
}
