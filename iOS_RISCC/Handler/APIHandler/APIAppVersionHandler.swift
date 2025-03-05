//
//  APIAppVersionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON

private let instance = APIAppVersionHandler()

class APIAppVersionHandler: NSObject {

    class var shared: APIAppVersionHandler {
        return instance
    }
    
    func appVersion(success:@escaping((_ message:String)->Void), failure:@escaping((_ messsage:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_APP_VERSION)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("App Version Response: \(json)")
                    let status = json["status"].boolValue
                    if status {
                        DBAppVersionHandler.shared.deleteAppVersion()
                        let dataJSON = json["data"]
                        let appVersionJSON = dataJSON["appVersion"]
                        for appVersionDict in appVersionJSON.arrayValue {
                            let family = appVersionDict["family"].string!
                            if family == DEVICE_IOS {
                                let appVersion = DBAppVersion.init()
                                appVersion.loadDataFromServer(appVersion: appVersionDict)
                                appVersion.commit()
                            }
                        }
                        success("Success")
                    } else {
                        let message = json["message"].string!
                        failure(message)
                    }
                } else {
                    failure("Failure")
                }
            }
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
}
