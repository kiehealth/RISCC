//
//  APIFeedbackHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON

private let instance = APIFeedbackHandler()

class APIFeedbackHandler: NSObject {

    class var shared: APIFeedbackHandler {
        return instance
    }
    
    func postFeedback(postParameter:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_FEEDBACK)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            
            print("RequestURL: \(requestURL)")
            print("Parameters: \(parameters)")
            
            AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                print("Feedback Response: \(response)")
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
