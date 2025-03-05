//
//  CellNote.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

protocol CellNoteDelegate: class {
    func btnDeleteNote(note:DBNote)
}

class CellNote: UITableViewCell {
    
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var btnDelete: UIButton!
    
    var note: DBNote? = nil
    var delegate: CellNoteDelegate? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.btnDelete.setImage(UIImage.init(named: "delete_icon")?.imageWithColor(color: UIColor.init(hexString: Constants.Color.COLOR_BASE)), for: .normal)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func load(note:DBNote) {
        self.note = note
        self.lblTitle.text = note.title!
        self.lblDate.text = Util.dateToString_dd_MMM(date: note.date!)
        self.lblDesc.text = note.desc!
    }
    
    @IBAction func btnDeleteAction(_ sender: Any) {
        self.delegate?.btnDeleteNote(note: self.note!)
    }
    
}
