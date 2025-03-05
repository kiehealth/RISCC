//
//  CellSideMenu.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class CellSideMenu: UITableViewCell {

    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func load(sideMenu:SideMenu) {
        self.imgView.image = UIImage.init(named: sideMenu.image)?.imageWithColor(color: .white)
        self.lblTitle.text = sideMenu.title
    }

}
