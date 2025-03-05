//
//  WebVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import WebKit

class WebViewVC: UIViewController, WKUIDelegate, WKNavigationDelegate {

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var viewWeb: UIView!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    
    var header: String = ""
    var url: String? = nil
    var pdfSource: String = ""
    var webKitView: WKWebView!
    var isLink: Bool = false
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        
    }
    
    fileprivate func loadData() {
        self.lblHeader.text = NSLocalizedString(self.header, comment: "")
        DispatchQueue.main.async {
            print("URL: \(self.url!)")
            self.webKitView.load(URLRequest(url: URL(string: self.url!)!))
            self.showLoading(view: self.view, text: "")
            self.webKitView.navigationDelegate = self
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
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnBackAction(_ sender: Any) {
        if self.isLink {
            self.dismiss(animated: false, completion: nil)
        } else {
            self.navigationController?.popViewController(animated: false)
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
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        self.tabBarController?.tabBar.isHidden = true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
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
    // MARK: WKNavigationDelegate Methods

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        self.stopLoading(fromView: self.view)
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        self.stopLoading(fromView: self.view)
    }
    
    
}
