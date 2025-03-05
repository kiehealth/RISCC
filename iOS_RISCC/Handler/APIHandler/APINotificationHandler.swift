//
//  APINotificationHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SharkORM
import SwiftyJSON

private let instance = APINotificationHandler()

class APINotificationHandler: NSObject {
    
    class var shared: APINotificationHandler {
        return instance
    }
    
    func requestNotification(page:Int, sizePerPage:Int, success:@escaping((_ message:String, _ totalRecord:Int)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_NOTIFICATION)?pageNumber=\(page)&pageSize=\(sizePerPage)"
            
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Notification Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        DBNotificationHandler.shared.deleteAllNotifications()
                        let totalRecord = json["totalRecord"].intValue
                        let dataJSON = json["data"]
                        let notificationJSON = dataJSON["list"]
                        
                        for notificationDict in notificationJSON.arrayValue {
                            let dbNotification = DBNotification()
                            dbNotification.loadDataFromServer(data: notificationDict)
                            dbNotification.commit()
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
    
    func postNotification(postParameters:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_NOTIFICATION)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameters) as! Parameters
            print("RequestURL: \(requestURL)")
            let jsonPrameters = JSON(parameters)
            print("Parameters: \(jsonPrameters)")
            
            AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                print("Post Notification Response: \(response)")
                if let result = response.value {
                    let json = JSON(result)
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
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
