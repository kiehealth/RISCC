//
//  FeedbackVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift

class FeedbackVC: UIViewController, UITextViewDelegate {

    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var btnMenu: UIButton!
    @IBOutlet weak var txtTopic: UITextField!
    @IBOutlet weak var txtFeedback: UITextView!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
//        self.btnMenu.setImage(UIImage.init(named: "ic_menu")?.imageWithColor(color: .white), for: .normal)
        self.lblHeader.text = NSLocalizedString("Feedback", comment: "")
        self.txtFeedback.layer.borderColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BORDER).cgColor
        self.txtTopic.placeholder = NSLocalizedString("Topic", comment: "")
        self.txtFeedback.text = NSLocalizedString("Feedback", comment: "")
        self.btnSubmit.setTitle(NSLocalizedString("Submit", comment: ""), for: .normal)
    }
    
    fileprivate func loadData() {
        
    }
    
    fileprivate func resetInputView(){
        self.txtTopic.text = ""
        self.txtFeedback.text = ""
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
        
        if (self.txtTopic.text?.count)! > 0 {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Topic cannot be Empty.", comment: ""))
        }
        
        if (self.txtFeedback.text.count > 0) && (self.txtFeedback.text != NSLocalizedString("Feedback", comment: "")) {
            status = true
        } else {
            return Validation(status: false, message: NSLocalizedString("Feedback cannot be Empty", comment: ""))
        }
        
        return Validation(status: status, message: NSLocalizedString("Success", comment: ""))
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
    
    @IBAction func btnSubmitAction(_ sender: Any) {
        let validation = self.validation()
        if validation.status {
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(self.txtTopic.text!, forKey: "title")
            postParameter.setValue(self.txtFeedback.text!, forKey: "description")
            postParameter.setValue(DEVICE_IOS, forKey: "runningOS")
            postParameter.setValue(UIDevice.modelName, forKey: "phoneModel")

            self.showLoading(view: self.view, text: "")
            APIFeedbackHandler.shared.postFeedback(postParameter: postParameter, success: { (message) in
                self.stopLoading(fromView: self.view)
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
                self.txtTopic.text = ""
                self.txtFeedback.text = ""
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
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        self.tabBarController?.tabBar.isHidden = true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.FeedbackIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.FeedbackOut.rawValue)
        self.tabBarController?.tabBar.isHidden = false
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
    // MARK: UITextViewDelegate Methods
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if self.txtFeedback == textView {
            self.txtFeedback.textColor = #colorLiteral(red: 0, green: 0, blue: 0, alpha: 1)
            if self.txtFeedback.text == "Feedback" || self.txtFeedback.text == "Återkoppling" {
                self.txtFeedback.text = ""
            }
        }
    }

}
