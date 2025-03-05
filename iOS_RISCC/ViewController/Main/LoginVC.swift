//
//  LoginVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift

class LoginVC: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var lblHeaderTitle: UILabel!
    @IBOutlet weak var txtUserName: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var btnForgotPassword: UIButton!
    @IBOutlet weak var viewLoginBlock: UIView!
    @IBOutlet weak var lblSignIn: UILabel!
    @IBOutlet weak var viewSignIn: UIView!
    @IBOutlet weak var viewEmail: UIView!
    @IBOutlet weak var viewPassword: UIView!
    @IBOutlet weak var btnSighUp: UIButton!
    
    let appDelegate = UIApplication.shared.delegate as! AppDelegate

    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.viewEmail.backgroundColor = UIColor(white: 176.0 / 255.0, alpha: 0.05)
        self.viewPassword.backgroundColor = UIColor(white: 176.0 / 255.0, alpha: 0.05)
        UITextField.connectFields(fields: [self.txtUserName, self.txtPassword])
        self.viewLoginBlock.isHidden = true
        self.txtPassword.disableAutoFill()
        let forgotPassword = "\(NSLocalizedString("Forgot Password", comment: ""))?"
        self.btnForgotPassword.setAttributedTitle(Util.setUnderlineToString(text: forgotPassword, color: .init(hexString: Constants.Color.COLOR_BASE)), for: .normal)
        
        self.txtUserName.placeholder = NSLocalizedString("Email", comment: "")
        self.txtPassword.placeholder = NSLocalizedString("Password", comment: "")
        self.lblSignIn.text = NSLocalizedString("Sign In", comment: "")
        
        let signInAttributeOne = [NSAttributedString.Key.foregroundColor: UIColor.black, NSAttributedString.Key.font: UIFont.systemFont(ofSize: 12)]
        let signInAttributeTwo = [NSAttributedString.Key.foregroundColor: UIColor.init(hexString: Constants.Color.COLOR_BASE), NSAttributedString.Key.font: UIFont.systemFont(ofSize: 12), NSAttributedString.Key.underlineStyle : NSUnderlineStyle.single.rawValue] as [NSAttributedString.Key : Any]
        
        let signInOne = NSMutableAttributedString(string: NSLocalizedString("Don't have account?", comment: ""), attributes: signInAttributeOne)
        let signInTwo = NSMutableAttributedString(string: NSLocalizedString("Sign Up", comment: ""), attributes: signInAttributeTwo)
        
        let signInCombination = NSMutableAttributedString()
        
        signInCombination.append(signInOne)
        signInCombination.append(signInTwo)
        
        self.btnSighUp.setAttributedTitle(signInCombination, for: .normal)
    }
    
    fileprivate func loadData() {
        self.loadPublicResourceFile()
        self.signInViewValidation()
    }
    
    fileprivate func loadLanguageSetting() {
//        self.btnSignIn.setTitle("Sign In".localized(), for: .normal)
    }
    
    fileprivate func isValid()->Bool {
        return true
    }
    
    private func showLoading(view:UIView, text:String){
        let loadingNotification = MBProgressHUD.showAdded(to: view, animated: true)
        loadingNotification.mode = MBProgressHUDMode.indeterminate
        loadingNotification.label.text = text
    }

    private func stopLoading(fromView:UIView){
        MBProgressHUD.hide(for: fromView, animated: true)
    }
    
    private func validation() -> Validation {
        var status: Bool = false
        
        if (self.txtUserName.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Username empty.")
        }
        
        if (self.txtPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Password is empty", comment: ""))
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
    }
    
    private func saveNotes() {
        let noteList = DBNoteHandler.shared.getNotesThatAreNotSync()
        let loggedUser = DBUserHandler.shared.getLoggedUser()
        if noteList.count > 0 {
            var notes = [[String: Any]]()
            for case let dbNote as DBNote in noteList {
//                let note = ["description": dbNote.desc!, "title": dbNote.title!]
//                notes.append(note)
                //            }
                let postParameter = NSMutableDictionary.init()
//                postParameter.setValue(notes, forKey: "notes")
                postParameter.setValue(loggedUser?.userId, forKey: "userId")
                postParameter.setValue(dbNote.title!, forKey: "title")
                postParameter.setValue(dbNote.desc!, forKey: "description")
                
                APINoteHandler.shared.saveOrUpdateNotes(postParameter: postParameter, isSync: false, method: .post, success: { (message) in
                    print(message)
                }) { (message) in
                    print(message)
                }
            }
        }
        
        let unsyncedUpdateNoteList = DBNoteHandler.shared.getNotesThatAreUnsyncedUpdate()
        if unsyncedUpdateNoteList.count > 0 {
            for case let note as DBNote in unsyncedUpdateNoteList {
                let postParameter = NSMutableDictionary.init()
                postParameter.setValue(note.noteId!, forKey: "id")
                postParameter.setValue(note.title!, forKey: "title")
                postParameter.setValue(note.desc!, forKey: "description")
                postParameter.setValue(loggedUser?.userId, forKey: "userId")
                
                APINoteHandler.shared.saveOrUpdateNotes(postParameter: postParameter, isSync: false, method: .put, success: { (message) in
                    self.stopLoading(fromView: self.view)
                    Util.clearQuestionOrAnswerData()
                    self.loadAppVersionAPI()
//                    self.moveToNextVC()
                }) { (message) in
                    self.stopLoading(fromView: self.view)
                    print(message)
                }
            }
        } else {
            self.stopLoading(fromView: self.view)
            Util.clearQuestionOrAnswerData()
//            self.moveToNextVC()
            self.loadAppVersionAPI()
        }
        
        
    }
    
    private func moveToNextVC() {
//            let tutorialVC = UIStoryboard.init(name: STORYBOARD_MAIN, bundle: nil).instantiateViewController(withIdentifier: IDENTIFIER_TUTORIAL_VC) as! TutorialVC
//            self.navigationController?.pushViewController(tutorialVC, animated: false)
        let mainTabVC = UIStoryboard.init(name: Constants.Storyboard.MAIN_TAB, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.MAIN_TAB_VC) as! MainTabVC
        self.navigationController?.pushViewController(mainTabVC, animated: false)
    }
    
    private func loadAnalytics() {
        APIAnalyticsHandler.shared.requestAnalytics(success: { (message) in
            print(message)
            Util.postAnalytics(analyticsType: ANALYTICS_TYPE.UserIn.rawValue)
        }) { (message) in
            print(message)
        }
    }
    
    private func signInViewValidation() {
        if self.txtUserName.text != "" || self.txtPassword.text != "" {
            self.viewSignIn.backgroundColor = UIColor(hexString: Constants.Color.COLOR_BASE)
            self.viewSignIn.isUserInteractionEnabled = true
        } else  {
            self.viewSignIn.backgroundColor = UIColor(hexString: Constants.Color.BUTTON_LIGHT_GRAY)
            self.viewSignIn.isUserInteractionEnabled = false
        }
    }
    
    private func loadPublicResourceFile() {
        APIResourceFileHandler.shared.getPublicResourceFile { (message) in
            
        } failure: { (message) in
            print(message)
        }
    }
    
    private func loadAppVersionAPI() {
        APIAppVersionHandler.shared.appVersion { (message) in
            let appVersion = DBAppVersionHandler.shared.getAppVersion()
            if appVersion.forceUpdate {
                let appUpdateVC = UIStoryboard.init(name: Constants.Storyboard.UTILITY, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.APP_UPDATE_VC) as! AppUpdateVC
                appUpdateVC.modalTransitionStyle = .crossDissolve
                appUpdateVC.modalPresentationStyle = .overCurrentContext
                appUpdateVC.dbAppVersion = appVersion
                self.present(appUpdateVC, animated: false, completion: nil)
            } else {
                self.moveToNextVC()
            }
        } failure: { (message) in
            print(message)
        }

    }
    
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnSignInAction(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
            let phoneNumber = (self.txtUserName.text?.contains("+"))! ? self.txtUserName.text! : "+\(self.txtUserName.text!)"
            
            let dbUser = DBUser.init()
            dbUser.userName = Util.validateEmail(enteredEmail: self.txtUserName.text!) ? self.txtUserName.text! : phoneNumber
            dbUser.password = self.txtPassword.text!
            self.showLoading(view: self.view, text: "")
            APIUserHandler.shared.login(user: dbUser, success: { (message) in
//                self.stopLoading(fromView: self.view)
                self.txtUserName.text = ""
                self.txtPassword.text = ""
                self.loadAnalytics()
                self.saveNotes()
            }) { (message) in
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
        } else {
            self.view.makeToast(validation.message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    @IBAction func btnRegisterAction(_ sender: Any) {
        let registerVC = UIStoryboard.init(name: Constants.Storyboard.MAIN, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.REGISTER_VC) as! RegisterVC
        self.navigationController?.pushViewController(registerVC, animated: false)
    }
    
    @IBAction func btnForgotPasswordAction(_ sender: Any) {
        let forgotPasswordVC = UIStoryboard.init(name: Constants.Storyboard.MAIN, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.FORGOT_PASSWORD_VC) as! ForgotPasswordVC
        self.navigationController?.pushViewController(forgotPasswordVC, animated: false)
    }
    
    @IBAction func txtUsernameEditingChanged(_ sender: Any) {
        self.signInViewValidation()
    }
    
    @IBAction func txtPasswordEditingChanged(_ sender: Any) {
        self.signInViewValidation()
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
//        self.loadLanguageSetting()
        
        let loggedUser = DBUserHandler.shared.getLoggedUser()
        if loggedUser != nil {
            self.viewLoginBlock.isHidden = false
            APIUserHandler.shared.login(user: loggedUser!, success: { (message) in
                self.viewLoginBlock.isHidden = true
//                self.loadAppVersionAPI()
                self.loadAnalytics()
                self.saveNotes()
            }) { (message) in
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
                self.viewLoginBlock.isHidden = true
            }
        }
        
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
    }
    
    // MARK: -
    // MARK: Delegate Methods

    // MARK: -
    // MARK: UITextFieldDelegate Methods
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == self.txtPassword && !self.txtPassword.isSecureTextEntry {
            self.txtPassword.isSecureTextEntry = true;
        }
        return true
    }
}
