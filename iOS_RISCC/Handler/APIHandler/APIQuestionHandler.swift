//
//  APIQuestionHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Alamofire
import SharkORM
import SwiftyJSON

private let instance = APIQuestionHandler()

class APIQuestionHandler: NSObject {
    
    class var shared: APIQuestionHandler {
        return instance
    }
    
    func requestQuestion(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_QUESTION_UNANSWERED)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Questionnaire Response: \(json)")
                    let status = json["status"].boolValue
                    if status {
                        let dataJSON = json["data"]
                        let questionnaireJSON = dataJSON["questionnaire"]
                        let questionJSON = questionnaireJSON["questions"]
                        
                        DBQuestionHandler.shared.deleteAllQuestions()
                        DBQuestionOptionHandler.shared.deleteAllQuestionOption()
                        DBQuestionnaireHandler.shared.deleteAllQuestionnaire()
                        
                        let questionnaire = DBQuestionnaire()
                        questionnaire.questionnaireId = questionnaireJSON["id"].stringValue
                        print("QuestionnaireId: \(questionnaire.questionnaireId!)")
                        questionnaire.commit()
                        
                        for questionDict in questionJSON.arrayValue {
                            let question = DBQuestion.init()
                            question.parseQuestion(data: questionDict)
                            question.commit()
                            
                        }
                        let message = json["message"].stringValue
                        success(message)
                    } else {
                        DBQuestionHandler.shared.deleteAllQuestions()
                        DBQuestionOptionHandler.shared.deleteAllQuestionOption()
                        DBQuestionnaireHandler.shared.deleteAllQuestionnaire()
                        let message = json["message"].stringValue
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
    
    func requestActiveGroupQuestionnaries(success:@escaping((_ message:String, _ activeQuestionnaires:NSMutableArray)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_GROUP_QUESTIONNAIRE_ACTIVE)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Group Questionnaire Active Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    
                    if status {
                        DBActiveQuestionnaireHandler.shared.deleteAllActiveQuestionnaires()
                        DBQuestionHandler.shared.deleteAllQuestions()
                        DBQuestionOptionHandler.shared.deleteAllQuestionOption()
                        DBQuestionnaireHandler.shared.deleteAllQuestionnaire()
                        
                        let dataJSON = json["data"]
                        let questionnairiesJSON = dataJSON["groupQuestionnaires"].arrayValue
                        let activeQuestionnaires = NSMutableArray.init()
                        for listDict in questionnairiesJSON {
                            let activeQuestionnaire = DBActiveQuestionnaire.init()
                            activeQuestionnaire.loadDataFromServer(data: listDict)
//                            activeQuestionnaire.commit()
                            activeQuestionnaires.add(activeQuestionnaire)
                        }
                        success(message, activeQuestionnaires)
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
    
    func requestPaginatedQuestionnaire(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_PAGINATED_LIST_OF_QUESTIONNAIRE)?pageNumber=0&pageSize=10"
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("PaginatedQuestionnaire response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    
                    if status {
                        DBActiveQuestionnaireHandler.shared.deleteAllActiveQuestionnaires()
                        DBQuestionHandler.shared.deleteAllQuestions()
                        DBQuestionOptionHandler.shared.deleteAllQuestionOption()
                        DBQuestionnaireHandler.shared.deleteAllQuestionnaire()
                        DBQuestionTypeHandler.shared.deleteAllQuestionType()
                        
                        let dataJSON = json["data"]
                        let listJSON = dataJSON["list"].arrayValue
                        for listDict in listJSON {
                            let activeQuestionnaire = DBActiveQuestionnaire.init()
                            activeQuestionnaire.loadDataFromServer(data: listDict)
                            activeQuestionnaire.commit()
                        }
                        success(message)
                    } else {
                        failure(message)
                    }
                }
            }
        } else {
            failure(Constants.Message.MSG_NETWORK_FAIL)
        }
    }
    
    func requestQuestionUnansweredByQuestionnaire(groupId:Int, questionnaierId:Int, success:@escaping((_ message:String, _ welcomeText: String, _ thankYouText: String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
                let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_QUESTION_UNANSWERED_BY_QUESTIONNAIRE)?questionnaireId=\(questionnaierId)&groupQuestionnaireId=\(groupId)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
//                print("Question by Questionnaire response: \(response)")
                if let result = response.value {
                    let json = JSON(result)
                    print("Questionnaire Response: \(json)")
                    let status = json["status"].boolValue
                    
                    
                    DBQuestionHandler.shared.deleteAllQuestions()
                    DBQuestionOptionHandler.shared.deleteAllQuestionOption()
                    DBQuestionnaireHandler.shared.deleteAllQuestionnaire()
                    DBQuestionTypeHandler.shared.deleteAllQuestionType()
                    
                    if status {
                        let dataJSON = json["data"]
                        let listJSON = dataJSON["list"]
                        let thankYouMessage = dataJSON["welcome"]["thankYouText"].stringValue
                        let welcomeMessage = dataJSON["welcome"]["welcomeText"].stringValue
                        let questionnaireJSON = listJSON["questionnaire"]
                        
                        let questionnaire = DBQuestionnaire()
                        questionnaire.questionnaireId = questionnaireJSON["id"].stringValue
                        questionnaire.commit()
                        
                        for questionDict in listJSON.arrayValue {
                            let question = DBQuestion.init()
                            question.parseQuestion(data: questionDict)
                            question.commit()
                            
                        }
                        let message = json["message"].stringValue
                        success(message, welcomeMessage, thankYouMessage)
                    } else {
                        let message = json["message"].stringValue
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
    
    func requestGroupQuestionnaire(success:@escaping((_ message:String, _ questionnaires:NSMutableArray)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_GROUP_QUESTIONNAIRE)"
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Group Questionnaire Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    
                    if status {
                        DBActiveQuestionnaireHandler.shared.deleteAllActiveQuestionnaires()

                        let dataJSON = json["data"]
                        let questionnairiesJSON = dataJSON["groupQuestionnaires"].arrayValue
                        let questionnaires = NSMutableArray.init()
                        for listDict in questionnairiesJSON {
                            let questionnaire = DBActiveQuestionnaire.init()
                            questionnaire.loadDataFromServer(data: listDict)
//                            activeQuestionnaire.commit()
                            questionnaires.add(questionnaire)
                        }
                        success(message, questionnaires)
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
    
    func requestRisccValue(for groupQuestionnaireId:Int, success:@escaping((_ message:String, _ moreInfo:String?)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            guard let user = DBUserHandler.shared.getLoggedUser() else { return }
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_RISCC_VALUE)?questionnaireIds=\(groupQuestionnaireId)&userIds=\(user.userId ?? "-1")"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("RISCC Value Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        let dataJSON = json["data"]
                        let risccValues = dataJSON["risccValue"]
                        
                        if risccValues[0].exists() {
                            let risccValue = risccValues[0]
                            let message = risccValue["message"].stringValue
                            let moreInfo: String? = risccValue["moreInfo"].stringValue
                            success (message, moreInfo)
                        } else {
                            success ("NA", nil)
                        }
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
    
    func requestAnswerByGroupQuestionnaier(groupQuestionnaireId:Int, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_ANSWER_BY_GROUP_QUESTIONNAIRE)\(groupQuestionnaireId)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Answer by Group Questionnaire Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        DBAnswerDataHandler.shared.deleteAllAnswerData()
                        DBAnswerOptionHandler.shared.deleteAllAnswerOption()
                        DBAnsweredQuestionHandler.shared.deleteAllAnsweredQuestion()
                        
                        let dataJSON = json["data"]
                        let answersJSON = dataJSON["list"]
                        
                        for answerDict in answersJSON.arrayValue {
                            let answerData = DBAnswerData()
                            answerData.loadDataFromServer(data: answerDict)
                            answerData.commit()
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
    
    
    func requestFinishGroupQuestionnaier(groupQuestionnaireId:Int, success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_POST_FINISH_GROUP_QUESTIONNAIRE)"
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(groupQuestionnaireId, forKey: "groupQuestionnaireId")
            let parameters: Parameters = NSDictionary.init(dictionary: postParameter) as! Parameters
            let param = JSON(parameters)

            AF.request(requestURL, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Finish Group Questionnaire Response: \(json)")
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
    
    func requestAnsweredQuestions(success:@escaping((_ message:String)->Void), failure:@escaping((_ message:String)->Void)) {
        if Util.isNetworkAvailable() {
            let requestURL = "\(URL_BASE)\(Constants.API.URL_GET_QUESTION_ANSWERED)"
            print("RequestURL: \(requestURL)")
            
            AF.request(requestURL, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Util.getHeader()).responseJSON { (response) in
                if let result = response.value {
                    let json = JSON(result)
                    print("Answered Questions Response: \(json)")
                    let message = json["message"].stringValue
                    let status = json["status"].boolValue
                    if status {
                        let dataJSON = json["data"]
                        let answeredQuestionJSON = dataJSON["questions"]
                        
//                        DBAnsweredQuestionHandler.shared.deleteAllAnsweredQuestions()
//                        DBAnswerHandler.shared.deleteAllAnswers()
//                        for answeredQuestionDict in answeredQuestionJSON.arrayValue {
//                            let answeredQuestion = DBAnsweredQuestion.init()
//                            answeredQuestion.parseUser(data: answeredQuestionDict)
//                            answeredQuestion.commit()
//                        }
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
