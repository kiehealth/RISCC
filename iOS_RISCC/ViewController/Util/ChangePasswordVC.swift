//
//  ChangePasswordVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift

class ChangePasswordVC: UIViewController {

    @IBOutlet weak var txtOldPassword: RISCCUITextField!
    @IBOutlet weak var txtNewPassword: RISCCUITextField!
    @IBOutlet weak var txtConfirmNewPassword: RISCCUITextField!
    @IBOutlet weak var btnChangePassword: UIButton!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    
    var loggedUser: DBUser? = nil
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
//        self.txtOldPassword.placeholder = NSLocalizedString("Old Password", comment: "")
//        self.txtNewPassword.placeholder = NSLocalizedString("New Password", comment: "")
//        self.txtConfirmNewPassword.placeholder = NSLocalizedString("Confirm New Password", comment: "")
    }
    
    fileprivate func loadData() {
        self.loggedUser = DBUserHandler.shared.getLoggedUser()
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
    
    fileprivate func validation()->Validation {
        var status: Bool = true
        
        
        if (self.txtOldPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Old password is empty.", comment: ""))
        }
        
        if (self.txtOldPassword.text == self.loggedUser?.password!) {
            status = true
        } else {
            return Validation(status: false, message: "Old Password not valid.")
        }
        
        if (self.txtNewPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "New Password empty.")
        }
        
        if (self.txtConfirmNewPassword.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: "Confirm New Password empty.")
        }
        
        if (self.txtNewPassword.text == self.txtConfirmNewPassword.text) {
            status = true
        } else {
            return Validation(status: false, message: "New Password doesnot match.")
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
    
    @IBAction func btnBackAction(_ sender: Any) {
        DispatchQueue.main.async {
//            self.navigationController?.popViewController(animated: false)
            self.dismiss(animated: false, completion: nil)
        }
    }
    
    @IBAction func btnChangePassword(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
            self.loggedUser?.password = self.txtOldPassword.text!
            self.loggedUser?.newPassword = self.txtNewPassword.text!
            
            self.showLoading(view: self.view, text: "")
            APIUserHandler.shared.changePassword(user: self.loggedUser!, success: { (messageChangePassword, user) in
                print(messageChangePassword)
                APIUserHandler.shared.login(user: user, success: { (message) in
                    self.stopLoading(fromView: self.view)
                    self.view.makeToast(messageChangePassword, duration: Constants.Timer.TOAST_DURATION, position: .center)
                    DispatchQueue.main.async {
                        
                        self.txtOldPassword.text = ""
                        self.txtNewPassword.text = ""
                        self.txtConfirmNewPassword.text = ""
                    }
                }) { (message) in
                    self.stopLoading(fromView: self.view)
                    self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
                }
            }) { (message) in
                print(message)
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
        } else {
            print(validation.message)
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
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
//        self.tabBarController?.tabBar.isHidden = true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.ChangePasswordIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.ChangePasswordOut.rawValue)
//        self.tabBarController?.tabBar.isHidden = false

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
