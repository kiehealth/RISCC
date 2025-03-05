//
//  APIAnonymousHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON

private let instance = APIAnonymousHandler()
extension CharacterSet {
    static let rfc3986Unreserved = CharacterSet(charactersIn: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~")
}
class APIAnonymousHandler: NSObject {
    
    class var shared: APIAnonymousHandler {
        return instance
    }
    
    func signUp(postParameter:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_SIGN_UP)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            print("RequestURL: \(requestURL)")
            print("Post Parameter: \(parameters)")
            
            AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("SignUp Response: \(json)")
                    let status = json["status"].boolValue
                    if status {
                        success(NSLocalizedString("Success", comment: ""))
                    } else {
                        let message = json["message"].stringValue
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
    
    func signUpVerificationCode(postParameter:NSMutableDictionary, success:@escaping((_ message:String, _ id:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_GET_SIGN_UP_VERIFICATION_CODE)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            print("RequestURL: \(requestURL)")
            print("Post Parameter: \(parameters)")
            
//            let manager = AF.SessionManager.default
            
//            manager.session.configuration.timeoutIntervalForRequest = TimeInterval(300000 * 1000) //5 Minute
            
            
            AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("SignUp Verification Code: Resposne: \(json)")
                    let status = json["status"].boolValue
                    if status {
                        let message = json["message"].stringValue
                        let data = json["data"]
                        let verificationData = data["verification"]
                        
                        let id = verificationData["id"].stringValue
                        success(message, id)
                    } else {
                        let message = json["message"].stringValue
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
    
    func requestPasswordResetVerificationCode(identity:String, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            
//            let identityEncoded = identity.addingPercentEncoding(withAllowedCharacters: .rfc3986Unreserved)
//            let requestURL = "\(URL_BASE)\(URL_GET_PASSWORD_RESET_VERIFICATION_CODE)?identity=\(identityEncoded!)"
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_PASSWORD_RESET_VERIFICATION_CODE)?identity=\(identity)"
            print("RequestURL: \(requestURL)")

            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Password Reset Verification Code Response: \(json)")
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
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
    
    func resetPassword(postParameter:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_PUT_RESET_PASSWORD)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            
            print("RequestURL: \(requestURL)")
            print("Parameter: \(parameters)")
            
            AF.request(requestURL, method: .put, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Reset Password Response: \(json)")
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
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
    
    
}
