//
//  DBInfoHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBInfoHandler()

class DBInfoHandler: NSObject {
    
    class var shared: DBInfoHandler {
        return instance
    }
    
    func getInfo(success:@escaping((_ infoList:NSMutableArray)->Void), failure:@escaping((_ message:String)->Void)) {
        do {
            let results: SRKResultSet = DBInfo.query().order("title").fetch()
            let infoList = NSMutableArray.init()
            for case let info as DBInfo in results {
                infoList.add(info)
            }
            success(infoList)
        } catch {
            failure("No info exist.")
        }
    }
    
    func deleteAllInfo() {
        DBInfo.query().fetch().remove()
    }

}
