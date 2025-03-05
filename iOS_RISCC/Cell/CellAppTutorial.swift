//
//  CellAppTutorial.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellAppTutorial: UICollectionViewCell {
    
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var lblDesc: UILabel!
    
    func load(tutorial: TutorialModel) {
        self.imgView.image = UIImage.init(named: tutorial.image)
    }
    
}
