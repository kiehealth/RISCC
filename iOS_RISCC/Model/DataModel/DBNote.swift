//
//  DBNote.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBNote: SRKObject {
    
    @objc dynamic var noteId: String?
    @objc dynamic var title: String?
    @objc dynamic var date: NSDate?
    @objc dynamic var desc: String?
    @objc dynamic var isSync: Bool = false
    @objc dynamic var unsyncedUpdate: Bool = false
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["noteId": "", "title" : "", "date" : NSDate(), "desc" : "", "isSync" : false, "unsyncedUpdate" : false]
    }
    
    override init() {
        super.init()
        self.noteId = ""
        self.title = ""
        self.date = NSDate()
        self.desc = ""
    }
    
    func saveData(noteId:String, title:String, desc:String, isSync:Bool, unsyncedUpdate:Bool) {
        self.noteId = noteId
        self.title = title
        self.date = NSDate()
        self.desc = desc
        self.isSync = isSync
        self.unsyncedUpdate = unsyncedUpdate
    }
    
    func loadDataFromServer(data:JSON) {
        self.noteId = data["id"].stringValue
        self.title = data["title"].stringValue
        self.date = Util.dateFromUnixTime(unixTimeInterval: data["createdDateTime"].doubleValue)
        self.desc = data["description"].stringValue
        self.isSync = true
        self.unsyncedUpdate = false
    }
}
