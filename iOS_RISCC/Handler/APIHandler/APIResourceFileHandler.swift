//
//  APIResourceFileHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SharkORM
import SwiftyJSON

private let instance = APIResourceFileHandler()

class APIResourceFileHandler: NSObject {
    
    class var shared: APIResourceFileHandler {
        return instance
    }
    
    func getResourceFile(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_RESOURCE_FILE)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Resource File Response: \(json)")
                    let status = json["status"].boolValue
                    let message = json["message"].stringValue
                    if status {
                        let dataJSON = json["data"]
                        let resourceFilesJSON = dataJSON["resourceFiles"]
                        for resourceFileDict in resourceFilesJSON.arrayValue {
                            let resourceFile = DBResourceFile.init()
                            resourceFile.loadDataFromServer(data: resourceFileDict)
                            resourceFile.commit()
                        }
                        success(message)
                    } else {
                        failure(message)
                    }
                } else {
                    failure("failure")
                }
            }
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }

    func getPublicResourceFile(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_PUBLIC_RESOURCE_FILE)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Public Resource File Response: \(json)")
                    let status = json["status"].boolValue
                    let message = json["message"].stringValue
                    if status {
                        DBResourceFileHandler.shared.deleteAllResourceFiles()
                        
                        let dataJSON = json["data"]
                        let resourceFilesJSON = dataJSON["resourceFiles"]
                        for resourceFileDict in resourceFilesJSON.arrayValue {
                            let resourceFile = DBResourceFile.init()
                            resourceFile.loadDataFromServer(data: resourceFileDict)
                            resourceFile.commit()
                        }
                        success(message)
                    } else {
                        failure(message)
                    }
                } else {
                    failure("failure")
                }
            }
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
}
