//
//  CellMyContact.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellMyContactDelegate: class {
    func btnDelete(myContact: DBMyContact)
    func btnEdit(myContact: DBMyContact)
}

class CellMyContact: UITableViewCell {
    
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblService: UILabel!
    @IBOutlet weak var imgCall: UIImageView!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var btnDelete: UIButton!
    @IBOutlet weak var btnEdit: UIButton!
    
    var myContact: DBMyContact? = nil
    var delegate: CellMyContactDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.imgCall.image = self.imgCall.image?.imageWithColor(color: Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE))
        self.btnEdit.setImage(UIImage.init(named: "edit")?.imageWithColor(color: UIColor.black), for: .normal)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }
    
    func load(myContact:DBMyContact) {
        self.myContact = myContact
        self.lblService.text = myContact.service!
        self.lblName.text = myContact.name!
        self.lblPhone.text = myContact.phoneNumber!
        self.lblEmail.text = myContact.email!
    }

    @IBAction func btnDeleteAction(_ sender: Any) {
        self.delegate?.btnDelete(myContact: self.myContact!)
    }
    
    @IBAction func btnEditAction(_ sender: Any) {
        self.delegate?.btnEdit(myContact: self.myContact!)
    }
}
