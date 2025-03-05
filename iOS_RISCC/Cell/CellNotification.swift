//
//  CellNotification.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellNotificationDelegate {
    func moveToNotificationDetail(notification:DBNotification)
}

class CellNotification: UITableViewCell {

    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblDesc: UILabel!
    
    var delegate: CellNotificationDelegate!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func load(notification:DBNotification) {
        self.lblTitle.text = notification.title!
//        self.lblDate.text = Util.dateToString_MMM_dd_yyyy(date: notification.date!)
        self.lblDate.text =  Util.dateToString_MMM_dd_yyyy_hh_mm_a(date: notification.date!)
        self.lblDesc.text = notification.desc!
        
        if notification.notificationId == DiaryHandler.shared.notificaitonId {
            DiaryHandler.shared.notificaitonId = ""
            self.delegate.moveToNotificationDetail(notification: notification)
        }
    }

}
