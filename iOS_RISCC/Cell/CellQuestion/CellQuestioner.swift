//
//  CellQuestioner.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import Cosmos

class CellQuestioner: UICollectionViewCell, UITableViewDataSource, UITableViewDelegate, CellQuestionTypeSelectOneDelegate, CellQuestionTypeSelectMoreDelegate {
    
    @IBOutlet weak var lblParentTitle: UILabel!
    @IBOutlet weak var lblParentTitleTopConstraints: NSLayoutConstraint!
    @IBOutlet weak var lblParentBody: UILabel!
    @IBOutlet weak var lblParentBodyTopConstraints: NSLayoutConstraint!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblBody: UILabel!
    @IBOutlet weak var tblQuestionType: UITableView!
    @IBOutlet weak var tblQuestionTypeHeight: NSLayoutConstraint!
    @IBOutlet weak var viewRating: CosmosView!
    
    var question = DBQuestion.init()
    var isOptionSelected: Bool = false
    var questionTypeSelectOneIndex: Int = 0
    var questionTypeSelectMoreIndex: [Int] = []
    var selectMoreOtherIndexPath: IndexPath? = nil
    var tableViewHeight: CGFloat = 10.0
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.tblQuestionType.estimatedRowHeight = 44.0
        self.lblParentTitleTopConstraints.constant = 0
        self.viewRating.rating = 0
        self.viewRating.settings.fillMode = .full
        self.viewRating.didTouchCosmos = { rating in
            DiaryHandler.shared.question = self.question
            DiaryHandler.shared.rating = rating
            let questionOption = self.question.questionOptions[Int(rating) - 1] as! DBQuestionOption
            DiaryHandler.shared.questionOption = questionOption
        }
    }
    
    func loadQuestion(question:DBQuestion) {
        DispatchQueue.main.async {
            self.question = question
            self.tblQuestionType.reloadData()
            print("Question: \(self.question.title!)")            
            self.lblParentTitle.text = ""
            self.lblParentTitleTopConstraints.constant = 0.0
            self.lblParentBody.text = ""
            self.lblParentBodyTopConstraints.constant = 0.0
            self.lblTitle.text = "\(question.title!)"
            self.lblBody.text = question.body!
            
            if self.question.questionType.title == QUESTION_TYPE.Rating.rawValue {
                self.tblQuestionType.isHidden = true
                self.viewRating.settings.totalStars = self.question.questionOptions.count
            } else {
                self.tblQuestionType.isHidden = false
            }
        }
    }
    
    // MARK: -
    // MARK: UITableViewDataSource, UITableViewDelegate Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if self.question.questionType.title == QUESTION_TYPE.DataInput.rawValue {
            return 1
        } else if self.question.questionType.title == QUESTION_TYPE.SelectOne.rawValue {
            return question.questionOptions.count
        } else if self.question.questionType.title == QUESTION_TYPE.SelectMore.rawValue {
            return question.questionOptions.count
        } else if self.question.questionType.title == QUESTION_TYPE.Rating.rawValue {
            return question.questionOptions.count
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if self.question.questionType.title == QUESTION_TYPE.DataInput.rawValue {
            let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.QUESTION_TYPE_DATA_INPUT, for: indexPath) as! CellQuestionTypeDataInput
            cell.load(question: question)
            return cell
            
        } else if self.question.questionType.title == QUESTION_TYPE.SelectOne.rawValue {
            let questionOption = question.questionOptions[indexPath.row] as! DBQuestionOption
            let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.QUESTION_TYPE_SELECT_ONE, for: indexPath) as! CellQuestionTypeSelectOne
            cell.delegate = self
            cell.deselectBeforeLoading()
            cell.loadQuestionOption(questionOption: questionOption, question: self.question, indexPath: indexPath)
            return cell
            
        } else if self.question.questionType.title == QUESTION_TYPE.SelectMore.rawValue {
            let questionOption = question.questionOptions[indexPath.row] as! DBQuestionOption
            let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.QUESTION_TYPE_SELECT_MORE, for: indexPath) as! CellQuestionTypeSelectMore
            cell.delegate = self
            cell.deselect()
            cell.loadQuestionOption(questionOption: questionOption, question: question, indexPath: indexPath)
            return cell
            
        } else if self.question.questionType.title == QUESTION_TYPE.Rating.rawValue {
            let questionOption = self.question.questionOptions[indexPath.row] as! DBQuestionOption
            let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.QUESTION_TYPE_RATING, for: indexPath) as! CellQuestionTypeRating
            cell.loadQuestionOption(questionOption: questionOption, question: self.question)
            cell.deselect()
            return cell
        } else {
            return UITableViewCell.init()
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if self.question.questionType.title == QUESTION_TYPE.SelectOne.rawValue {
            let cell = tableView.cellForRow(at: indexPath) as! CellQuestionTypeSelectOne
            tableView.selectRow(at: indexPath, animated: false, scrollPosition: .none)
            cell.select()
            self.questionTypeSelectOneIndex = indexPath.row
        } else if self.question.questionType.title == QUESTION_TYPE.SelectMore.rawValue {
            let cell = tableView.cellForRow(at: indexPath) as! CellQuestionTypeSelectMore
            print("Selected: \(cell.optionSelected)")
            if cell.optionSelected {
                if self.selectMoreOtherIndexPath != nil && indexPath != self.selectMoreOtherIndexPath {
                    self.tblQuestionType.delegate?.tableView?(self.tblQuestionType, didSelectRowAt: selectMoreOtherIndexPath!)
                }
                cell.select()
                self.questionTypeSelectMoreIndex.append(indexPath.row)
                
            } else {
                if let index = self.questionTypeSelectMoreIndex.index(of: indexPath.row) {
                    self.questionTypeSelectMoreIndex.remove(at: index)
                }
                cell.deselect()
            }
        } else if self.question.questionType.title == QUESTION_TYPE.Rating.rawValue {
            let cell = tableView.cellForRow(at: indexPath) as! CellQuestionTypeRating
            cell.select()
        }
    }
    
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        if self.question.questionType.title == QUESTION_TYPE.SelectOne.rawValue {
            let cell = tableView.cellForRow(at: indexPath) as! CellQuestionTypeSelectOne
            cell.deselect()

        } else if self.question.questionType.title == QUESTION_TYPE.Rating.rawValue {
            let cell = tableView.cellForRow(at: indexPath) as! CellQuestionTypeRating
            cell.deselect()
        }
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if self.question.questionType.title == QUESTION_TYPE.DataInput.rawValue {
            self.tableViewHeight += cell.frame.size.height
            self.tblQuestionTypeHeight.constant = self.tableViewHeight
            self.tableViewHeight = 10.0
        } else if self.question.questionType.title == QUESTION_TYPE.SelectOne.rawValue {
            self.tableViewHeight += cell.frame.size.height
            self.tblQuestionTypeHeight.constant = self.tableViewHeight
            if indexPath.row == question.questionOptions.count - 1 {
                self.tableViewHeight = 10.0
            }
        } else if self.question.questionType.title == QUESTION_TYPE.SelectMore.rawValue {
            self.tableViewHeight += cell.frame.size.height
            self.tblQuestionTypeHeight.constant = self.tableViewHeight
            if indexPath.row == question.questionOptions.count - 1 {
                self.tableViewHeight = 10.0
            }
        } else if self.question.questionType.title == QUESTION_TYPE.Rating.rawValue {
            self.tableViewHeight += cell.frame.size.height
            self.tblQuestionTypeHeight.constant = self.tableViewHeight
            if indexPath.row == question.questionOptions.count - 1 {
                self.tableViewHeight = 10.0
            }
        }
    }
    
    // MARK: -
    // MARK: CellQuestionTypeSelectOneDelegate Methods
    
    func textFieldTapped() {
        let indexPath = IndexPath(row: self.questionTypeSelectOneIndex, section: 0)
        print("IndexPath: \(indexPath)")
        self.tblQuestionType.delegate?.tableView!(tblQuestionType, didDeselectRowAt: indexPath)
    }
    
    func optionAutoSelect(indexPath: IndexPath) {
        print("IndexPath: \(indexPath.row)")
        self.tblQuestionType.delegate?.tableView?(self.tblQuestionType, didSelectRowAt: indexPath)
    }
    
    // MARK: -
    // MARK: CellQuestionTypeSelectMoreDelegate Methods
    
    func questionTypeSelectMoreTextFieldTapped(indexPath: IndexPath) {
        self.questionTypeSelectMoreIndex.forEach { (index) in
            let indexPath = IndexPath(row: index, section: 0)
            self.tblQuestionType.delegate?.tableView!(self.tblQuestionType, didSelectRowAt: indexPath)
        }
        self.tblQuestionType.delegate?.tableView?(self.tblQuestionType, didSelectRowAt: indexPath)
        self.selectMoreOtherIndexPath = indexPath
    }
}
