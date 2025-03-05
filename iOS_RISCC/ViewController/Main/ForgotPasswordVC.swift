//
//  ForgotPasswordVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift

class ForgotPasswordVC: UIViewController {

    @IBOutlet weak var lblForgotPassword: UILabel!
    @IBOutlet weak var lblForgotPasswordMessage: UILabel!
    @IBOutlet weak var btnSignIn: UIButton!
    @IBOutlet weak var txtIdentity: UITextField!
    @IBOutlet weak var viewEmail: UIView!
    @IBOutlet weak var viewRequestVerificationCode: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var btnSubmit: RISCCUIButton!
    
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var viewResetPassword: UIView!
    @IBOutlet weak var txtVerificationCode: RISCCUITextField!
    @IBOutlet weak var txtNewPassword: RISCCUITextField!
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.btnBack.setImage(UIImage.init(named: "back")?.imageWithColor(color: UIColor.init(hexString: Constants.Color.BUTTON_GRAY)), for: .normal)
        self.viewResetPassword.isHidden = true
        self.txtIdentity.placeholder = NSLocalizedString("Email", comment: "")
        let yourAttributes = [NSAttributedString.Key.foregroundColor: UIColor.black, NSAttributedString.Key.font: UIFont.systemFont(ofSize: 18)]
        let yourOtherAttributes = [NSAttributedString.Key.foregroundColor: #colorLiteral(red: 0.3019607843, green: 0.462745098, blue: 1, alpha: 1), NSAttributedString.Key.font: UIFont.systemFont(ofSize: 18)] as [NSAttributedString.Key : Any]
        
        let partOne = NSMutableAttributedString(string: NSLocalizedString("Verification code has been sent to your email.Please check your email and ", comment: ""), attributes: yourAttributes)
        let partTwo = NSMutableAttributedString(string: NSLocalizedString("update your password.", comment: ""), attributes: yourOtherAttributes)
        
        let combination = NSMutableAttributedString()
        
        combination.append(partOne)
        combination.append(partTwo)
        
        self.lblTitle.attributedText = combination
        
        self.lblForgotPassword.text = "\(NSLocalizedString("Forgot Password", comment: ""))?".uppercased()
        self.lblForgotPasswordMessage.text = NSLocalizedString("Please enter your registered email address or phone number, we will get back to you with the reset password confirmation (One Time Password)", comment: "")
        self.btnSubmit.setTitle(NSLocalizedString("Submit", comment: ""), for: .normal)
        
        let signInAttributeOne = [NSAttributedString.Key.foregroundColor: UIColor.black, NSAttributedString.Key.font: UIFont.systemFont(ofSize: 12)]
        let signInAttributeTwo = [NSAttributedString.Key.foregroundColor: UIColor.init(hexString: Constants.Color.COLOR_BASE), NSAttributedString.Key.font: UIFont.systemFont(ofSize: 12), NSAttributedString.Key.underlineStyle : NSUnderlineStyle.single.rawValue] as [NSAttributedString.Key : Any]
        
        let signInOne = NSMutableAttributedString(string: NSLocalizedString("Remember Password?", comment: ""), attributes: signInAttributeOne)
        let signInTwo = NSMutableAttributedString(string: NSLocalizedString("Sign In", comment: ""), attributes: signInAttributeTwo)
        
        let signInCombination = NSMutableAttributedString()
        
        signInCombination.append(signInOne)
        signInCombination.append(signInTwo)
        
        self.btnSignIn.setAttributedTitle(signInCombination, for: .normal)
        
        self.viewEmail.layer.borderWidth = 1
        self.viewEmail.layer.borderColor = UIColor.init(hexString: Constants.Color.COLOR_BORDER).cgColor

    }
    
    fileprivate func loadData() {
        
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
        
        if (self.txtIdentity.text?.count)! > 0 {
            status = true
        } else {
             return Validation(status: false, message: NSLocalizedString("Email is empty.", comment: ""))
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
    }
    
    private func resetPasswordValidation() -> Validation {
        var status: Bool = false
        
        if (self.txtVerificationCode.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Verification code empty.")
        }
        
        if (self.txtNewPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("New Password empty.", comment: ""))
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
    }
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnSignInAction(_ sender: Any) {
        self.btnBackAction(self.btnBack!)
    }
    
    @IBAction func btnSendAction(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
            self.showLoading(view: self.view, text: "")
            var emailOrPhonenumber = self.txtIdentity.text!
            if Util.validateEmail(enteredEmail: emailOrPhonenumber){
                //
            } else {
                emailOrPhonenumber = (self.txtIdentity.text?.contains("+"))! ? self.txtIdentity.text! : "+\(self.txtIdentity.text!)"

            }
            APIAnonymousHandler.shared.requestPasswordResetVerificationCode(identity: emailOrPhonenumber, success: { (message) in
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
                self.viewRequestVerificationCode.isHidden = true
                self.viewResetPassword.isHidden = false
            }) { (message) in
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
        } else {
            self.stopLoading(fromView: self.view)
            self.view.makeToast(validation.message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    @IBAction func btnBackAction(_ sender: Any) {
        self.navigationController?.popViewController(animated: false)
    }
    
    @IBAction func btnResetPasswordAction(_ sender: Any) {
        let validation = self.resetPasswordValidation()
        if validation.status {
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(self.txtIdentity.text!, forKey: "emailAddress")
            postParameter.setValue(self.txtVerificationCode.text!, forKey: "verificationCode")
            postParameter.setValue(self.txtNewPassword.text!, forKey: "newPassword")
            
            self.showLoading(view: self.view, text: "")
            APIAnonymousHandler.shared.resetPassword(postParameter: postParameter, success: { (message) in
                self.stopLoading(fromView: self.view)
                self.viewResetPassword.isHidden = true
                self.btnBackAction(self.btnBack)
            }) { (message) in
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
        } else {
            self.view.makeToast(validation.message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
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

}
