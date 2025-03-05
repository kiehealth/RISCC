//
//  APIUserHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SwiftyJSON
import SharkORM

private let instance = APIUserHandler()

class APIUserHandler: NSObject {
    
    class var shared: APIUserHandler {
        return instance
    }
    
    func login(user:DBUser, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            print("Login API called.")
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_LOGIN)"
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(DEVICE_IOS, forKey: "devicePlatform")
            if DiaryHandler.shared.deviceToken == "00000000000000" || DiaryHandler.shared.deviceToken.count == 0{
                print("Your device token not retried yet!")
            } else {
                postParameter.setValue(DiaryHandler.shared.deviceToken, forKey: "deviceToken")
                postParameter.setValue(PUSH_NOTIFICATION_MODE, forKey: "iosNotificationMode")
            }
            postParameter.setValue(user.password, forKey: "password")
            postParameter.setValue(user.userName, forKey: "username")
            
            let parameter: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            
            print("RequestURL: \(requestURL)")
            
            print("Parameter: \(JSON(parameter))")
            
            AF.request(requestURL, method: .post, parameters: parameter, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in

//                print("User Login Response: \(response)")
                if let result = response.value {
                    let json = JSON(result)
                    print("User Login Response: \(json)")
                    let status = json["status"].boolValue
                    let message = json["message"].stringValue

                    if status {
                        DBUserHandler.shared.deleteAllUser()
                        let dataJSON = json["data"]
                        let token = dataJSON["token"].stringValue
                        let chatServer = dataJSON["chatServer"].stringValue
                        print("Token: \(token)")
                        let userData = dataJSON["user"]
                        
                        let newUser = DBUser.init()
                        newUser.token = token
                        newUser.userName = user.userName
                        newUser.password = user.password
                        newUser.parseUser(user: userData)                        
                        newUser.commit()
                        
                        success(message)
                    } else {
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
    
    func updateUser(postParameter:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let loggedUser = DBUserHandler.shared.getLoggedUser()
            let requestUrl = "\(URL_BASE)\(Constants.API.URL_PUT_UPDATE_USER)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            
            let jsonParameter = JSON(parameters)
            print("JSONParameter: \(jsonParameter)")
            print("RequestUrl: \(requestUrl)")
            print("Parameter: \(parameters)")
            print("Header: \(Util.getHeader()!)")
            
            AF.request(requestUrl, method: .put, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("User Update Response: \(json)")
                    let status = json["status"].boolValue
                    let message = json["message"].stringValue
                    
                    if status {
                        let dataJSON = json["data"]
                        let userData = dataJSON["user"]

                        loggedUser!.parseUser(user: userData)
                        loggedUser!.commit()
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
    
    func changePassword(postParameter:NSMutableDictionary, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_PUT_CHANGE_PASSWORD)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            
            print("RequestURL: \(requestURL)")
            print("Parameters: \(parameters)")
            
            AF.request(requestURL, method: .put, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                print("Change Password Response: \(response.value)")
            }
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
    
    func changePassword(user:DBUser, success:@escaping((_ message:String, _ user:DBUser)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestUrl = "\(URL_BASE)\(Constants.API.URL_PUT_CHANGE_PASSWORD)"
            
            
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(user.newPassword!, forKey:"newPassword")
            postParameter.setValue(user.password!, forKey:"oldPassword")
            postParameter.setValue(user.email!, forKey: "emailAddress")
            
            let putParameter: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            print("RequestUrl: \(requestUrl)")
            print("Parameters: \(putParameter)")
            
            AF.request(requestUrl, method: .put, parameters: putParameter, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Change Password Response: \(json)")
                    let status = json["status"]
                    if status == true {
                        user.password = user.newPassword!
                        user.commit()
                        let message = json["message"].string!
                        success(message, user)
                    } else {
                        let message = json["message"].string!
                        failure("\(message)")
                    }
                } else {
                    failure("failure")
                }
            }
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
    
    func requestLogout(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestUrl = "\(URL_BASE)\(Constants.API.URL_GET_LOGOUT)"
            AF.request(requestUrl, method: .put, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Logout Response: \(json)")
                    let status = json["status"]
                    if status == true {
                        let message = json["message"].string!
                        success(message)
                    } else {
                        let message = json["message"].string!
                        failure("\(message)")
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
