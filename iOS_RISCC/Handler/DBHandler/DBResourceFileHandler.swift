//
//  DBResourceFileHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBResourceFileHandler()

class DBResourceFileHandler: NSObject {
    
    class var shared: DBResourceFileHandler {
        return instance
    }
    
    func getAboutUsResourceFile() -> DBResourceFile {
        let langStr = Locale.current.languageCode
        var title = RESOURCE_FILE_TYPE.AboutUsES.rawValue
        if langStr == "en" {
            title = RESOURCE_FILE_TYPE.AboutUs.rawValue
        } else if langStr == "sv" {
            title = RESOURCE_FILE_TYPE.AboutUsSe.rawValue
        }
        return getResourceFileBy(title: title)
    }
    
    func getPrivacyPolicyResourceFile() -> DBResourceFile {
        let langStr = Locale.current.languageCode
        var title = RESOURCE_FILE_TYPE.PrivacyPolicyES.rawValue
        if langStr == "en" {
            title = RESOURCE_FILE_TYPE.PrivacyPolicy.rawValue
        } else if langStr == "sv" {
            title = RESOURCE_FILE_TYPE.PrivacyPolicySe.rawValue
        }
        return getResourceFileBy(title: title)
    }
    
    func getConsentResourceFile() -> DBResourceFile {
        let langStr = Locale.current.languageCode
        var title = RESOURCE_FILE_TYPE.ConsentES.rawValue
        if langStr == "en" {
            title = RESOURCE_FILE_TYPE.Consent.rawValue
        } else if langStr == "sv" {
            title = RESOURCE_FILE_TYPE.ConsentSe.rawValue
        }
        return getResourceFileBy(title: title)
    }
    
    func getResourceFileBy(title:String) -> DBResourceFile {
        let results: SRKResultSet = DBResourceFile.query().where(withFormat: "title = %@", withParameters: [title]).fetch()
        if results.count > 0 {
            let resourceFile = results[0] as! DBResourceFile
//            return "\(URL_BASE)\(resourceFile.url!)"
            return resourceFile
        } else {
            return DBResourceFile()
        }
    }
    
    func deleteAllResourceFiles() {
        DBResourceFile.query().fetch().remove()
    }

}
