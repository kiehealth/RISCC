//
//  APIInfoHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON
import SharkORM

private let instance = APIInfoHandler()

class APIInfoHandler: NSObject {
    
    class var shared: APIInfoHandler {
        return instance
    }
    
    func requestLinks(page:Int, sizePerPage:Int, success:@escaping((_ message:String, _ totalRecord:Int)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_LINK)?pageNumber=\(page)&pageSize=\(sizePerPage)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Link Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        let totalRecord = json["totalRecord"].intValue
                        let dataJSON = json["data"]
                        let infoJSON = dataJSON["list"]
                        DBInfoHandler.shared.deleteAllInfo()
                        
                        for infoDict in infoJSON.arrayValue {
                            let info = DBInfo.init()
                            info.loadDataFromServer(data: infoDict)
                            info.commit()
                        }
                        success(message, totalRecord)
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
    
    func requestAboutUs(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_ABOUT_US)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("About Us Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        let dataJSON = json["data"]
                        let aboutUsJSON = dataJSON["aboutUs"]
                        DBAboutUsHandler.shared.deleteAll()
                        
                        let aboutUs = DBAboutUs.init()
                        aboutUs.loadDataFromServer(data: aboutUsJSON)
                        aboutUs.commit()
                        
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
