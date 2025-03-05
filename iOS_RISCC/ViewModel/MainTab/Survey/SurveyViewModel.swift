//
//  SurveyViewModel.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit

class SurveyViewModel: NSObject {

    func isAnswerForSingleSlectionTypeQuestion() -> Bool{
        let question = DiaryHandler.shared.question
        let questionOption = DiaryHandler.shared.questionOption
        return (question != nil && questionOption != nil && question?.questionType.title == QUESTION_TYPE.SelectOne.rawValue)
    }
    
    func isOneAnswerSelectedFromMultipleSlectionTypeQuestion() -> Bool{
        let question = DiaryHandler.shared.question
        let questionOption = DiaryHandler.shared.questionOption
        return (question != nil && questionOption != nil && question?.questionType.title == QUESTION_TYPE.SelectMore.rawValue)
    }
    
    
    func isMoreThanOneAnswerSelectedFromMultipleSlectionTypeQuestion() -> Bool{
        let question = DiaryHandler.shared.question
        let questionOptionList = DiaryHandler.shared.questionOptionList
        return (question != nil && questionOptionList.count > 0 && question?.questionType.title == QUESTION_TYPE.SelectMore.rawValue)
    }
    
    func isAnswerForRatingTypeQuestion() -> Bool {
        let question = DiaryHandler.shared.question
        let questionOption = DiaryHandler.shared.questionOption
        return (question != nil && questionOption != nil && question?.questionType.title == QUESTION_TYPE.Rating.rawValue)
    }
}
