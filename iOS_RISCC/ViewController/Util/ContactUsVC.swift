//
//  ContactUsVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import MessageUI

class ContactUsVC: UIViewController, MFMessageComposeViewControllerDelegate, MFMailComposeViewControllerDelegate {

    @IBOutlet var viewOutSide: UIView!
    @IBOutlet weak var lblProjectResponsible: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var btnOk: UIButton!
    
    var aboutUs: DBAboutUs? = nil
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        self.lblProjectResponsible.text = NSLocalizedString("Project responsible", comment: "")
        self.nameLabel.text = NSLocalizedString("Name", comment: "")
        self.phoneLabel.text = NSLocalizedString("Phone", comment: "")
        self.emailLabel.text = NSLocalizedString("Email", comment: "")
    }
    
    fileprivate func loadData() {
        self.aboutUs = DBAboutUsHandler.shared.getAboutUs()
        if self.aboutUs != nil {
            self.lblName.text = ":  \((self.aboutUs?.name)!)"
            self.lblPhone.text = ":  \((self.aboutUs?.phone)!)"
            self.lblEmail.text = ":  \((self.aboutUs?.email)!)"
        }
    }
    
    fileprivate func loadAboutUs() {
        APIInfoHandler.shared.requestAboutUs(success: { (message) in
            self.loadData()
        }) { (message) in
            self.loadData()
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        if(touches.first?.view == self.viewOutSide){
            self.btnOkAction(self.btnOk)
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
    
    private func getPhoneNumber(phoneNumber:String) throws -> NSURL {
        let phone = "tel://"
        if NSURL(string: "\(phone)\(phoneNumber)") != nil {
            let url:NSURL = NSURL(string: "\(phone)\(phoneNumber)")!
            return url
        } else {
            throw ERROR_PHONE_NUMBER.invalid
        }
    }
    
    @objc private func alertControllerBackgroundTapped() {
        self.dismiss(animated: true, completion: nil)
    }
    
    private func configureMailController(email:String) -> MFMailComposeViewController {
            let mailComposerVC = MFMailComposeViewController()
            mailComposerVC.mailComposeDelegate = self
            mailComposerVC.setToRecipients(["\(email)"])
    //        mailComposerVC.setSubject("Hello")
    //        mailComposerVC.setMessageBody("How are you doing?", isHTML: false)
            return mailComposerVC
        }
        
        private func showMailError(type:String) {
            let sendMailErrorAlert = UIAlertController(title: "Could not send \(type)", message: "Your device could not send \(type)", preferredStyle: .alert)
            let dismiss = UIAlertAction(title: "Ok", style: .default, handler: nil)
            sendMailErrorAlert.addAction(dismiss)
            self.present(sendMailErrorAlert, animated: true, completion: nil)
        }
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnOkAction(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func lblPhoneAction(_ sender: Any) {
        do {
            var phoneNumber = self.aboutUs!.phone!
            phoneNumber = phoneNumber.components(separatedBy: " ").joined()
            let url = try self.getPhoneNumber(phoneNumber: phoneNumber)
//            let alert = UIAlertController(title: "Choose number to call", message: "Please choose which number you want to call", preferredStyle: .alert)
//
//            let firstNumberAction = UIAlertAction(title: "\(phoneNumber)", style: .default) { (action) in
                if #available(iOS 10.0, *) {
                    //iOS 10.0 and greater
                    UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
                } else {
                    //iOS 9
                    UIApplication.shared.openURL(url as URL)
                }
//            }
//            
//            alert.addAction(firstNumberAction)
//            self.present(alert, animated: true) {
//                alert.view.superview?.isUserInteractionEnabled = true
//                alert.view.superview?.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.alertControllerBackgroundTapped)))
//            }
        } catch {
            self.view.makeToast("Phone number: \(self.aboutUs!.phone!)", duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    @IBAction func lblEmailAction(_ sender: Any) {
        let mailComposeVC = self.configureMailController(email: self.aboutUs!.email!)
        if MFMailComposeViewController.canSendMail() {
            self.present(mailComposeVC, animated: true, completion: nil)
        } else {
            showMailError(type: "email")
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
        self.loadAboutUs()
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
    
    // MARK: -
    // MARK: MFMessageComposeViewControllerDelegate Methods
    
    func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        self.dismiss(animated: false, completion: nil)
    }
    
    // MARK: -
    // MARK: MFMailComposeViewControllerDelegate Methods
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: false, completion: nil)
    }

}
