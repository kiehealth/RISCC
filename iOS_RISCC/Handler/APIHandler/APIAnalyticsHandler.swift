//
//  APIAnalyticsHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON
import SharkORM

private let instance = APIAnalyticsHandler()

class APIAnalyticsHandler: NSObject {
    
    class var shared: APIAnalyticsHandler {
        return instance
    }
    
    func requestAnalytics(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_APP_ANALYTICS_TYPE)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Analytics Response: \(json)")
                    let status = json["status"].boolValue
                    let message = json["message"].stringValue
                    if status {
                        DBAnalyticsHandler.shared.deleteAllAnalytics()
                        let dataJSON = json["data"]
                        let analyticsTypeJSON = dataJSON["list"]
                        for analyticsDict in analyticsTypeJSON.arrayValue {
                            let analytics = DBAnalytics.init()
                            analytics.loadDataFromServer(data: analyticsDict)
                            analytics.commit()
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
    
    func postAppAnalytics(analyticsType:String, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            if Util.getHeader() != nil {
                let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_APP_ANALYTICS)"
                
                let analytics = DBAnalyticsHandler.shared.getAnalyticsTypeWith(title: analyticsType)
                if analytics != nil {
                    let parameters: Parameters = ["appAnalyticsKeyId" : analytics!.analyticId!,
                                                  "description" : analytics!.desc!,
                                                  "value": "\(Util.unixTimeFromNSDate(date: NSDate()))"] as Parameters
                    
                    print("RequestURL: \(requestURL)")
                    print("Parameters: \(parameters)")
                    
                    AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                        if let result = response.value {
                            let json = JSON(result)
                            print("Post Analytics Response: \(json)")
                            let status = json["status"].boolValue
                            let message = json["message"].stringValue
                            if status {
                                success(message)
                            } else {
                                failure(message)
                            }
                        } else {
                            failure("failure")
                        }
                    }
                }
            } else {
                failure("User not logged in.")
            }
            
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
    
}
