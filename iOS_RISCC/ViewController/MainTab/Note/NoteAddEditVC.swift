//
//  NoteAddEditVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift
import Reachability

class NoteAddEditVC: UIViewController, UITextViewDelegate {

    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var txtTitle: UITextField!
    @IBOutlet weak var txtDescription: UITextView!
    @IBOutlet weak var btnSave: UIButton!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    
    var note: DBNote? = nil
    var header: String? = nil
    var buttonTitle: String? = nil
    var isAddNote: Bool = false
    
    private var reachability: Reachability?
    
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.txtTitle.leftViewMode = UITextField.ViewMode.always
        self.txtTitle.leftView = Util.leftView()
        
        self.btnSave.setTitle(NSLocalizedString(self.buttonTitle!, comment: ""), for: .normal)
        self.txtDescription.layer.borderColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BORDER).cgColor
        do {
            self.reachability = try Reachability()
        } catch(let error) {
            print("Error occured while starting reachability notifications : \(error.localizedDescription)")

        }
    }
    
    fileprivate func loadData() {
        self.lblHeader.text = self.header!
//        self.btnSave
        if self.note != nil {
            self.txtTitle.text = self.note?.title!
            self.txtDescription.text = self.note?.desc!
            self.txtDescription.textColor = UIColor.black
        } else {
            self.note = DBNote.init()
            self.txtDescription.text = NSLocalizedString("Description", comment: "")
        }
        self.btnSaveViewValidation()
        

        
        self.reachability?.whenReachable = { _ in
            DispatchQueue.main.async {
                print("Online")
            }
        }
        
        self.reachability?.whenUnreachable = { _ in
            DispatchQueue.main.async {
                print("Offline")
            }
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.internetChanged), name: Notification.Name.reachabilityChanged, object: self.reachability)
    }
    
    @objc fileprivate func internetChanged(note:Notification) {
        let reachability = note.object as! Reachability
        if reachability.isReachable {
            if reachability.isReachableViaWiFi {
                DispatchQueue.main.async {
                    print("Wifi Available available....")
                }
            } else {
                DispatchQueue.main.async {
                    print("Phone")
                }
            }
        } else {
            DispatchQueue.main.async {
                print("Ofline")
            }
        }
    }
    
    fileprivate func isValid()->Bool {
        return true
    }
    
    private func showLoading(view:UIView, text:String){
        let loadingNotification = MBProgressHUD.showAdded(to: view, animated: true)
        loadingNotification.mode = MBProgressHUDMode.indeterminate
        //loadingNotification.labelText = text
    }
    
    private func stopLoading(fromView:UIView){
        MBProgressHUD.hide(for: fromView, animated: true)
    }
    
    private func validation() -> Validation {
        var status: Bool = false
        
        if (self.txtTitle.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Title is empty.", comment: ""))
        }
        
        if self.txtDescription.text != "Note Description Here." && self.txtDescription.text.count > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Description is empty.", comment: ""))
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
    }
    
    @objc private func alertControllerBackgroundTapped() {
        self.dismiss(animated: true, completion: nil)
    }
    
    private func btnSaveViewValidation() {
        if self.txtTitle.text != "" {
            self.btnSave.backgroundColor = UIColor(hexString: Constants.Color.COLOR_BASE)
            self.btnSave.isUserInteractionEnabled = true
        } else  {
            self.btnSave.backgroundColor = UIColor(hexString: Constants.Color.BUTTON_LIGHT_GRAY)
            self.btnSave.isUserInteractionEnabled = false
        }
    }
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnBackAction(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
            let alert = UIAlertController(title: "", message: NSLocalizedString("Your notes will be discarded, want to exit?", comment: ""), preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Exit", comment: ""), style: .default, handler: { (action) in
                self.navigationController?.popViewController(animated: false)
            }))
            alert.addAction(UIAlertAction(title: NSLocalizedString("No", comment: ""), style: .cancel, handler: nil))
            self.present(alert, animated: true) {
                alert.view.superview?.isUserInteractionEnabled = true
                alert.view.superview?.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.alertControllerBackgroundTapped)))
            }
        } else {
            self.navigationController?.popViewController(animated: false)
        }
    }
    
    @IBAction func btnSaveAction(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
                
            var notes = [[String: Any]]()
            let note = ["description": self.txtDescription.text!, "title": self.txtTitle.text!]
            notes.append(note)
            
            let loggedUser = DBUserHandler.shared.getLoggedUser()
            
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(loggedUser?.userId, forKey: "userId")
            if self.isAddNote {
//                postParameter.setValue(notes, forKey: "notes")
                postParameter.setValue(self.txtTitle.text!, forKey: "title")
                postParameter.setValue(self.txtDescription.text!, forKey: "description")
            } else {
                postParameter.setValue(self.note?.noteId!, forKey: "id")
                postParameter.setValue(self.txtTitle.text!, forKey: "title")
                postParameter.setValue(self.txtDescription.text!, forKey: "description")
            }
            
            if Util.isNetworkAvailable() {
                APINoteHandler.shared.saveOrUpdateNotes(postParameter: postParameter, isSync: true, method: self.isAddNote ? .post : .put, success: { (message) in
                    DiaryHandler.shared.isNoteAddedOrUpdated = true
                    self.navigationController?.popViewController(animated: false)
                }) { (message) in
                    print(message)
                }
            } else {
                DBNoteHandler.shared.getAlreadySavedData(noteId: (self.note?.noteId!)!, success: { (note) in
                    self.note?.saveData(noteId: note.noteId!, title: self.txtTitle.text!, desc: self.txtDescription.text!, isSync: self.isAddNote ? false : true, unsyncedUpdate: self.isAddNote ? false : true)
                    self.note?.commit()
                    self.navigationController?.popViewController(animated: false)
                }) { (message) in
                    let noteId = String(Util.unixTimeFromNSDate(date: NSDate()))
                    self.note?.saveData(noteId: noteId, title: self.txtTitle.text!, desc: self.txtDescription.text!, isSync: self.isAddNote ? false : true, unsyncedUpdate: self.isAddNote ? false : true)
                    self.note?.commit()
                    self.navigationController?.popViewController(animated: false)
                }
            }
        } else {
            self.view.makeToast(validation.message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    @IBAction func txtTitleEditingChanged(_ sender: Any) {
        self.btnSaveViewValidation()
    }
    
    // MARK: -
    // MARK: Object Methods
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func loadView() {
        super.loadView()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.configureView()
        self.loadData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NoteDetailIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NoteDetailOut.rawValue)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.removeObserver(self, name:Notification.Name.reachabilityChanged, object: nil)

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: -
    // MARK: UITextViewDelegate Methods

    func textViewDidBeginEditing(_ textView: UITextView) {
        if self.txtDescription == textView {
            self.txtDescription.textColor = UIColor.black
//            if self.txtDescription.text == "Note Description Here." || self.txtDescription.text == "Anteckningsbeskrivning här." {
            if self.txtDescription.text == NSLocalizedString("Description", comment: "") {
                self.txtDescription.text = ""
            }
        }
    }
    
}
