//
//  LinksDetailVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import MessageUI

class LinksDetailVC: UIViewController, MFMailComposeViewControllerDelegate {

    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var btnLink: UIButton!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var viewCall: UIView!
    @IBOutlet weak var viewCallHeight: NSLayoutConstraint!
    @IBOutlet weak var viewEmail: UIView!
    @IBOutlet weak var viewEmailHeight: NSLayoutConstraint!
    //    @IBOutlet weak var imgCall: UIImageView!
    
    var info = DBInfo.init()
    
    // MARK: -
    // MARK: Private Utility Methods
    
    fileprivate func configureView(){
//        self.btnLink.setImage(UIImage.init(named: "webLink")?.imageWithColor(color: UIColor.white), for: .normal)
//        self.imgCall.image = self.imgCall.image?.imageWithColor(color: Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE))
    }
    
    fileprivate func loadData() {
        self.lblTitle.text = self.info.title!
        self.lblDesc.text = self.info.desc!
        self.lblPhone.text = self.info.contactNumber!
        self.viewCall.isHidden = self.info.contactNumber! == "" ? true : false
        self.lblEmail.text = self.info.emailAddress!
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
    
    @IBAction func btnCloseAction(_ sender: Any) {
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func btnLinkAction(_ sender: Any) {
        let webViewVC = UIStoryboard.init(name: Constants.Storyboard.UTILITY, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.WEB_VIEW_VC) as! WebViewVC
        webViewVC.url = self.info.url!
        webViewVC.modalPresentationStyle = .fullScreen
        webViewVC.isLink = true
//        self.navigationController?.pushViewController(webViewVC, animated: false)
        self.present(webViewVC, animated: false, completion: nil)
    }
    
    @IBAction func lblEmailTapGestureRecongnizer(_ sender: Any) {
        let mailComposeVC = self.configureMailController(email: info.emailAddress!)
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
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.LinkDetailIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.LinkDetailOut.rawValue)
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
    // MARK: MFMailComposeViewControllerDelegate Methods
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: false, completion: nil)
    }

}
