//
//  DBResourceFile.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM
import SwiftyJSON

class DBResourceFile: SRKObject {

    @objc dynamic var fileId: String?
    @objc dynamic var title: String?
    @objc dynamic var desc: String?
    @objc dynamic var fileType: String?
    @objc dynamic var url: String?
    
    override class func defaultValuesForEntity() -> [String: Any] {
        return ["fileId": "", "title" : "", "desc" : "", "fileType" : "", "url" : ""]
    }
    
    func loadDataFromServer(data:JSON) {
        self.fileId = data["id"].stringValue
        self.title = data["title"].stringValue
        self.desc = data["description"].stringValue
//        self.fileType = data["fileType"].stringValue
        self.url = data["url"].stringValue        
    }
}
