//
//  RegisterVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import DropDown
import Toast_Swift
import WebKit

class RegisterVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, BEMCheckBoxDelegate, WKUIDelegate, WKNavigationDelegate, UITextFieldDelegate {

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblCreateNewProfile: RISCCUILabel!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var lblFirstName: UILabel!
    @IBOutlet weak var lblLastName: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblPassword: UILabel!
    @IBOutlet weak var lblConfirmPassword: UILabel!
    @IBOutlet weak var txtPhoneNumber: UITextField!
    @IBOutlet weak var txtFirstName: UITextField!
    @IBOutlet weak var txtLastName: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var txtConfirmPassword: UITextField!
    @IBOutlet weak var btnCamera: UIButton!
    @IBOutlet weak var checkBox: BEMCheckBox!
    @IBOutlet weak var btnAlreadyHaveAnAccount: UIButton!
    @IBOutlet weak var lblSignUp: UILabel!
    @IBOutlet weak var lblTermsAndServices: UILabel!
    
    //Consent
    @IBOutlet weak var viewConsentContainer: UIView!
    @IBOutlet weak var viewWeb: UIView!
    @IBOutlet weak var checkBoxConsent: BEMCheckBox!
    @IBOutlet weak var lblConsentDesc: UILabel!
    
    //Verification
    @IBOutlet weak var viewVerificationAlert: UIView!
    @IBOutlet weak var lblPhoneNumber: UILabel!
    @IBOutlet weak var txtCodeOne: UITextField!
    @IBOutlet weak var txtCodeTwo: UITextField!
    @IBOutlet weak var txtCodeThree: UITextField!
    @IBOutlet weak var txtCodeFour: UITextField!
    
    
    var maxDate = Calendar.current.date(byAdding: .year, value: 0, to: Date())
    var dateOfBirth = NSDate.init()
    var verificationId: String? = nil
    var txtVerificationCode: UITextField?
//    var dropDown = DropDown()
    var gender: String? = nil
    var textFields: [UITextField] = []
    var webKitView: WKWebView!
    var labels: [UILabel] = []
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.viewVerificationAlert.isHidden = true
        self.checkBox.boxType = .square
        self.checkBox.onCheckColor = UIColor.init(hexString: Constants.Color.COLOR_BASE)
        self.checkBox.onTintColor = UIColor.init(hexString: Constants.Color.COLOR_BASE)
        self.checkBox.tintColor = UIColor.init(hexString: Constants.Color.COLOR_BASE)
        self.checkBoxConsent.boxType = .square
        
//        self.btnBack.setImage(UIImage(named: "backward")?.imageWithColor(color: UIColor(hexString: Constants.Color.COLOR_COSTUME_GRAY)), for: .normal)
        self.imgProfile.layer.borderColor = UIColor.init(hexString: Constants.Color.COLOR_BASE).cgColor
        self.btnCamera.layer.borderColor = UIColor.init(hexString: Constants.Color.COLOR_BASE).cgColor
        let leftView = UIView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let countryCodeIdentifer: UILabel = UILabel(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        countryCodeIdentifer.text = "+"
        countryCodeIdentifer.font = UIFont(name: "Lato", size: 16.0)
        countryCodeIdentifer.textAlignment = .center
        
        let attributedStringTermsAndService = NSMutableAttributedString()
        attributedStringTermsAndService.append(NSAttributedString.init(string: "\(NSLocalizedString("I agree to the", comment: "")) "))
        attributedStringTermsAndService.append(NSAttributedString(string: NSLocalizedString("Terms of Service", comment: ""),
                                                   attributes: [NSAttributedString.Key.underlineStyle: NSUnderlineStyle.single.rawValue]))
        attributedStringTermsAndService.addAttribute(NSAttributedString.Key.foregroundColor, value: Util.hexStringToUIColor(hex: Constants.Color.COLOR_COSTUME_DARK_GRAY) , range: NSMakeRange(0, attributedStringTermsAndService.length))
        self.lblTermsAndServices.attributedText = attributedStringTermsAndService
        
        let attributedStringAlreadyHaveAccount = NSMutableAttributedString()
        attributedStringAlreadyHaveAccount.append(NSAttributedString.init(string: "\(NSLocalizedString("Already have an account?", comment: "")) "))
        attributedStringAlreadyHaveAccount.append(NSAttributedString(string: NSLocalizedString("Sign In", comment: ""),
                                                   attributes: [NSAttributedString.Key.underlineStyle: NSUnderlineStyle.single.rawValue]))
        attributedStringAlreadyHaveAccount.addAttribute(NSAttributedString.Key.foregroundColor, value: Util.hexStringToUIColor(hex: Constants.Color.COLOR_COSTUME_DARK_GRAY) , range: NSMakeRange(0, attributedStringAlreadyHaveAccount.length))
        self.btnAlreadyHaveAnAccount.setAttributedTitle(attributedStringAlreadyHaveAccount, for: .normal)
        
        self.lblSignUp.text = NSLocalizedString("Sign Up", comment: "")
        
        self.labels = [self.lblCreateNewProfile, self.lblFirstName, self.lblLastName, self.lblEmail, self.lblPhone, self.lblPassword, self.lblConfirmPassword]
        self.labels.forEach { (label) in
            label.text = NSLocalizedString(label.text!, comment: "")
        }
    }
    
    fileprivate func loadData() {
        DispatchQueue.main.async {
            let langStr = Locale.current.languageCode
            var dbResouceFile = DBResourceFileHandler.shared.getConsentResourceFile()
            let urlString = "\(URL_BASE)\(dbResouceFile.url ?? "")"
            
            if let url = URL(string: urlString) {
                self.webKitView.load(URLRequest(url: url))
                self.showLoading(view: self.view, text: "")
                self.webKitView.navigationDelegate = self
                self.lblConsentDesc.text = dbResouceFile.desc!
            } else { return }
            
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
        
        if (self.txtFirstName.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("First Name is empty.", comment: ""))
        }
        
        if (self.txtLastName.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Last Name is empty.", comment: ""))
        }
        
        if (self.txtEmail.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Email is empty", comment: ""))
        }
        
        if (self.txtPhoneNumber.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Phone number is empty", comment: ""))
        }
        
        if (self.txtPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Password is empty", comment: ""))
        }
        
        if (self.txtConfirmPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Confirm Password is empty", comment: ""))
        }
        
        if self.txtPassword.text == self.txtConfirmPassword.text {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Password and confirm password doesnot match.", comment: ""))
        }
        
        if self.checkBox.on {
            status = true
        } else {
            return Validation(status: false, message: "Please check terms of thes service")
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
    }
    
    private func verificationValidation() -> Validation {
        var status: Bool = false
        
        if (self.txtCodeOne.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Please Enter code.")
        }
        
        if (self.txtCodeTwo.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Please Enter code.")
        }
        
        if (self.txtCodeThree.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Please Enter code.")
        }
        
        if (self.txtCodeFour.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Please Enter code.")
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
    }
    
    private func getImagePickerActionSheet(){
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        actionSheet.addAction(UIAlertAction(title:"Camera", style: .default, handler: { (action: UIAlertAction) in
            let imagePickerController = UIImagePickerController()
            if UIImagePickerController.isSourceTypeAvailable(.camera){
                imagePickerController.delegate = self
                imagePickerController.sourceType = .camera
                self.present(imagePickerController, animated: true, completion: nil)
            } else {
                //                print("Camera not available")
            }
        }))
        actionSheet.addAction(UIAlertAction(title:"PhotoLibrary", style: .default, handler: { (action: UIAlertAction) in
            let imagePickerController = UIImagePickerController()
            imagePickerController.delegate = self
            imagePickerController.sourceType = .photoLibrary
            self.present(imagePickerController, animated: true, completion: nil)
        }))
        actionSheet.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        self.present(actionSheet, animated: true, completion: nil)
    }
    
    private func configurationTextFeild(textField:UITextField!) {
        if (textField) != nil {
            self.txtVerificationCode = textField!
            self.txtVerificationCode?.placeholder = "Verification Code"
        }
    }
    
    private func openAlertView() {
        let alert = UIAlertController(title: NSLocalizedString("Enter verification code that has been sent to your email.", comment: ""), message: "", preferredStyle: UIAlertController.Style.alert)
        alert.addTextField(configurationHandler: self.configurationTextFeild)
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (UIAlertAction) in
            self.loadRegisterAPI()
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    private func loadRegisterAPI() {
        let isValid = self.validation()
        if isValid.status {
            let verificationCode = "\(self.txtCodeOne.text!)\(self.txtCodeTwo.text!)\(self.txtCodeThree.text!)\(self.txtCodeFour.text!)"
            let verification = ["id": self.verificationId,
                                "verificationCode": verificationCode] as [String: Any]
            
            let postParameter = NSMutableDictionary.init()
//            postParameter.setValue("\(self.txtCountryCode.text!)\(self.txtPhoneNumber.text!)", forKey: "mobileNumber")
            postParameter.setValue("\(Util.includeCountryCodeIfItIsMobileNumber(inputStr: self.txtPhoneNumber.text!))", forKey: "mobileNumber")
            postParameter.setValue(DEVICE_IOS, forKey: "devicePlatform")
            postParameter.setValue(DiaryHandler.shared.deviceToken, forKey: "deviceToken")
            postParameter.setValue(PUSH_NOTIFICATION_MODE, forKey: "iosNotificationMode")
            
            postParameter.setValue("\(self.txtEmail.text!)", forKey: "emailAddress")
            postParameter.setValue(self.txtFirstName.text!, forKey: "firstName")
            postParameter.setValue(self.txtLastName.text!, forKey: "lastName")
            postParameter.setValue(self.txtPassword.text!, forKey: "password")
            postParameter.setValue(verification, forKey: "verification")
            postParameter.setValue("MALE", forKey: "gender")
//            postParameter.setValue(self.gender, forKey: "gender")
//            postParameter.setValue(Util.unixTimeFromNSDate(date: self.dateOfBirth), forKey: "dateOfBirth")
            postParameter.setValue(self.imgProfile.image?.base64EncodedString(), forKey: "image")
            
            self.showLoading(view: self.view, text: "")
            APIAnonymousHandler.shared.signUp(postParameter: postParameter, success: { (message) in
                self.stopLoading(fromView: self.view)
                self.btnBackAction(self.btnBack!)
            }) { (message) in
                self.stopLoading(fromView: self.view)
                self.viewVerificationAlert.isHidden = true
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
        } else {
            self.view.makeToast(isValid.message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    private func moveToWebViewVC(header:String, url:String, pdfSource:String) {
        let webViewVC = UIStoryboard.init(name: Constants.Storyboard.UTILITY, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.WEB_VIEW_VC) as! WebViewVC
        webViewVC.header = header
        webViewVC.url = url
        webViewVC.pdfSource = pdfSource
        self.navigationController?.pushViewController(webViewVC, animated: false)
    }
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnBackAction(_ sender: Any) {
        self.navigationController?.popViewController(animated: false)
    }
    
    @IBAction func btnCameraAction(_ sender: Any) {
        self.getImagePickerActionSheet()
    }
    
    @IBAction func btnGenderAction(_ sender: Any) {
//        self.dropDownView(button: self.btnGender, options: GENDER)
    }
    
    @IBAction func btnSignUpAction(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue("\(self.txtEmail.text!)", forKey: "emailAddress")
            postParameter.setValue("\(Util.includeCountryCodeIfItIsMobileNumber(inputStr: self.txtPhoneNumber.text!))", forKey: "mobileNumber")
            self.showLoading(view: self.view, text: "")
            APIAnonymousHandler.shared.signUpVerificationCode(postParameter: postParameter, success: { (message, id) in
                self.stopLoading(fromView: self.view)
                self.verificationId = id
                self.viewVerificationAlert.isHidden = false
                self.lblPhoneNumber.text = self.txtPhoneNumber.text!
                self.txtCodeOne.becomeFirstResponder()
            }) { (message) in
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
        } else {
            self.view.makeToast(validation.message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    @IBAction func btnTermsOfServiceAction(_ sender: Any) {
        let dbResouceFile = DBResourceFileHandler.shared.getPrivacyPolicyResourceFile()
        let urlString = "\(URL_BASE)\(dbResouceFile.url ?? "")"
        if URL(string: urlString) != nil {
            self.moveToWebViewVC(header: "Terms of Service", url: urlString, pdfSource: "")
        } else { return }
    }
    
    @IBAction func txtCodeOneEditingChanged(_ sender: Any) {
        if (self.txtCodeOne.text?.count)! > 0 {
            self.txtCodeOne.resignFirstResponder()
            self.txtCodeTwo.becomeFirstResponder()
        }
    }
    
    @IBAction func txtCodeTwoEditingChanged(_ sender: Any) {
        if (self.txtCodeTwo.text?.count)! > 0 {
            self.txtCodeTwo.resignFirstResponder()
            self.txtCodeThree.becomeFirstResponder()
        }
    }
    
    @IBAction func txtCodeThreeEditingChanged(_ sender: Any) {
        if (self.txtCodeThree.text?.count)! > 0 {
            self.txtCodeThree.resignFirstResponder()
            self.txtCodeFour.becomeFirstResponder()
        }
    }
    
    @IBAction func txtCodeFourEditingChanged(_ sender: Any) {
        if (self.txtCodeFour.text?.count)! > 0 {
            self.txtCodeFour.resignFirstResponder()
        }
    }
    
    @IBAction func btnVerifyAction(_ sender: Any) {
        let validation = self.verificationValidation()
        if validation.status {
            self.loadRegisterAPI()
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
        DispatchQueue.main.async {
            let webConfiguration = WKWebViewConfiguration()
            self.webKitView = WKWebView(frame: self.viewWeb.bounds, configuration: webConfiguration)
            self.webKitView.uiDelegate = self
            self.viewWeb.addSubview(self.webKitView)
        }
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
    // MARK: BEMCheckBoxDelegate Methods
    
    func didTap(_ checkBox: BEMCheckBox) {
        if checkBox == self.checkBoxConsent && checkBox.on {
            self.viewConsentContainer.isHidden = true
        }
    }
    
    // MARK: -
    // MARK: UIImagePickerControllerDelegate, UINavigationControllerDelegate Methods
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        let loadedImage = info[.originalImage] as! UIImage
        self.imgProfile.image = loadedImage
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    // MARK: -
    // MARK: WKNavigationDelegate Methods

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        self.stopLoading(fromView: self.view)
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        self.stopLoading(fromView: self.view)
    }

    // MARK: -
    // MARK: UITextFieldDelegate Methods
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField == self.txtPassword && !self.txtPassword.isSecureTextEntry) {
            self.txtPassword.isSecureTextEntry = true;
        } 
        if (textField == self.txtConfirmPassword && !self.txtConfirmPassword.isSecureTextEntry) {
            self.txtConfirmPassword.isSecureTextEntry = true;
        }
        return true
    }
}
