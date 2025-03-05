//
//  APIAnswerHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON
import SharkORM

private let instance = APIAnswerHandler()

class APIAnswerHandler: NSObject {
    
    class var shared: APIAnswerHandler {
        return instance
    }
    
    func answer(postParameter:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_ANSWER)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            let param = JSON(parameters)
            
            print("RequestURL: \(requestURL)")
            print("Parameter: \(param)")
//            print("Header: \(Util.getHeader())")
            
            AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (resposne) in
                if let result = resposne.value {
                    let json = JSON(result)
                    print("Answer Response: \(json)")
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
