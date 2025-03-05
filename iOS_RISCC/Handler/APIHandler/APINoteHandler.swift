//
//  APINoteHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SharkORM
import SwiftyJSON

private let instance = APINoteHandler()

class APINoteHandler: NSObject {

    class var shared: APINoteHandler {
        return instance
    }
    
    func requestNotes(page:Int, sizePerPage:Int, success:@escaping((_ message:String, _ totalRecord:Int)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_NOTES)"
            
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Note Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        let totalRecord = json["totalRecord"].intValue
                        let dataJSON = json["data"]
                        let noteJSON = dataJSON["list"]
                        for note in noteJSON.arrayValue {
                            DBNoteHandler.shared.getAlreadySavedData(noteId: note["id"].stringValue, success: { (dbNote) in
                                dbNote.loadDataFromServer(data: note)
                                dbNote.commit()
                            }, failure: { (message) in
                                let dbNote = DBNote.init()
                                dbNote.loadDataFromServer(data: note)
                                dbNote.commit()
                            })
                        }
                        success(message, totalRecord)
                    } else {
                        failure(message)
                    }
                }else {
                    failure(Constants.Message.MSG_NETWORK_FAIL)
                }
            }
        }
    }
    
    func saveOrUpdateNotes(postParameter:NSMutableDictionary, isSync:Bool, method:HTTPMethod, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_ADD_UPDATE_NOTE)"
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            
            print("RequestURL: \(requestURL)")
            print("Parameters: \(JSON(parameters))")
            
            AF.request(requestURL, method: method, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Add Note Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        let dataJSON = json["data"]
                        if method == .post {
                            let noteJSON = dataJSON["list"]
                            DBNoteHandler.shared.deleteAllNoteWithSyncValue(isSync: isSync)
                            for noteDict in noteJSON.arrayValue {
                                let noteId = noteDict["id"].stringValue
                                let dbNote = DBNote.init()
                                dbNote.loadDataFromServer(data: noteDict)
                                dbNote.commit()
                            }
                        } else {
                            let noteJSON = dataJSON["note"]
                            let noteId = noteJSON["id"].stringValue
                            DBNoteHandler.shared.getAlreadySavedData(noteId: noteId, success: { (dbNote) in
                                dbNote.loadDataFromServer(data: noteJSON)
                                dbNote.commit()
                            }, failure: { (message) in
                                let dbNote = DBNote.init()
                                dbNote.loadDataFromServer(data: noteJSON)
                                dbNote.commit()
                            })
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
    
    func deleteNote(noteId:String, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_DELETE_NOTE)\(noteId)"
            
            AF.request(requestURL, method: .delete, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Note delete response: \(json)")
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
