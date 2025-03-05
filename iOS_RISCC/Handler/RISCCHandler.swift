//
//  RISCCHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SwiftyJSON

private let instance = DiaryHandler()

class DiaryHandler: NSObject {
    
    class var shared: DiaryHandler {
        return instance
    }
    
    var questionType: String = ""
    var question: DBQuestion? = nil
    var questionOption: DBQuestionOption? = nil
    var questionOptionList = NSMutableArray.init()
    var rating: Double = 0.0
    
    var answeredQuestionType: String = ""
    var answeredQuestion: DBAnsweredQuestion? = nil
//    var answeredQuestionOption: DBAnsweredQuestionOption? = nil
    var answeredQuestionOptionList = NSMutableArray.init()
    var answeredQuestionRating: Double = 0.0
    
    var deviceToken: String = ""
    
    var answer: AnswerModel? = nil
    
    var isNoteAddedOrUpdated: Bool = false
    var isContactAddedOrUpdated: Bool = false
    var moveToNotificaitonVC: Bool = false
    var notificaitonId: String = ""
    var isFromNotification: Bool = false
    
    
    var onlineBuddies = NSMutableArray()

    
    /*Notification Relateted Infromations*/
    var isAppOpenedAfterTapingNotification = false
    var notificationType = "NONE"
    
    
}
