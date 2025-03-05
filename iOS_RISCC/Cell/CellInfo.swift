//
//  CellVideo.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellInfoDelegate: class {
    func btnLink(url:String)
    func viewCallTapped(info:DBInfo)
    func lblEmailTapped(info:DBInfo)
}

class CellInfo: UITableViewCell {

    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var btnLinkWidth: NSLayoutConstraint!
    @IBOutlet weak var btnLink: UIButton!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var lblEmailHeight: NSLayoutConstraint!
    @IBOutlet weak var imgCall: UIImageView!
    @IBOutlet weak var viewCall: UIView!
    @IBOutlet weak var viewCallHeight: NSLayoutConstraint!
    
    var info: DBInfo? = nil
    var delegate: CellInfoDelegate? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.viewCallTapped(_:)))
        self.viewCall.addGestureRecognizer(tapGesture)
        
        let emailTapGesture = UITapGestureRecognizer(target: self, action: #selector(self.lblEmailTapped))
        self.lblEmail.addGestureRecognizer(emailTapGesture)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func load(info:DBInfo) {
        self.info = info
        self.lblTitle.text = info.title!
        self.lblDesc.text = info.desc!
        self.viewCall.isHidden = info.contactNumber! == "" ? true : false
        self.lblPhone.text = info.contactNumber!
        self.lblEmail.text = info.emailAddress!
        if self.viewCall.isHidden && self.lblEmail.text == "" {
            self.viewCallHeight.constant = 0.0
            self.lblEmailHeight.constant = 0.0
        } else {
            self.viewCallHeight.constant = 18.0
            self.lblEmailHeight.constant = 18.0
        }
    }

    @IBAction func btnLinkAction(_ sender: Any) {
        self.delegate?.btnLink(url: (self.info?.url!)!)
    }
    
    @IBAction func viewCallTapped(_ sender: Any) {
        self.delegate?.viewCallTapped(info: self.info!)
    }
    
    @objc func lblEmailTapped() {
        self.delegate?.lblEmailTapped(info: self.info!)
    }
}
