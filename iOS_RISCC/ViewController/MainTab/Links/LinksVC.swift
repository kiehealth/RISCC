//
//  LinksVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import Toast_Swift
import MessageUI

class LinksVC: UIViewController, UITableViewDataSource, UITableViewDelegate, CellInfoDelegate, MFMailComposeViewControllerDelegate, SideMenuVCDelegate {
    
    @IBOutlet weak var btnMenu: UIButton!
    @IBOutlet weak var tblInfo: UITableView!
    @IBOutlet weak var btnProfile: UIButton!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var txtSearch: UITextField!
    @IBOutlet weak var viewEmptyMessage: UIView!
    @IBOutlet weak var lblEmptyMessage: UILabel!
    
    var infoList = NSMutableArray.init()
    var searchedInfoList: NSMutableArray? = nil
    var refreshControl = UIRefreshControl()
    var btnSearch = UIButton()
    var currentPage = 0
    var totalRecord = 0
    var sizePerPage = 10
    var canLoadMore: Bool = false
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.viewEmptyMessage.isHidden = true
        self.btnMenu.setImage(UIImage.init(named: "ic_menu")?.imageWithColor(color: .white), for: .normal)
        self.btnSearch.tintColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BORDER)
        self.txtSearch.placeholder = NSLocalizedString("Search", comment: "")
        self.lblEmptyMessage.text = NSLocalizedString("No link available!", comment: "")
//        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 15, height: 15))
//        imageView.contentMode = .scaleAspectFit
//        imageView.image = UIImage.init(named: "search")?.withRenderingMode(.alwaysTemplate)
////        self.btnSearch.setImage(image, for: .normal)
////        self.btnSearch.frame = CGRect(x: 0, y: 0, width: 50, height: 30)
////        self.btnSearch.contentMode = .scaleAspectFit
////        self.btnSearch.addTarget(self, action: #selector(self.btnSearchAction(_:)), for: .touchUpInside)
////        self.txtSearch.leftView = self.btnSearch
//        self.txtSearch.leftView = imageView
//        self.txtSearch.leftViewMode = .always
    }
    
    fileprivate func loadData() {
        self.loadInfo()
        self.showLoading(view: self.view, text: "")
        APIInfoHandler.shared.requestLinks(page: self.currentPage, sizePerPage: self.sizePerPage, success: { (message, totalRecord) in
            self.stopLoading(fromView: self.view)
            self.currentPage += 1
            self.totalRecord = totalRecord
            self.loadInfo()
        }) { (message) in
            self.stopLoading(fromView: self.view)
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    fileprivate func loadInfo() {
        DBInfoHandler.shared.getInfo(success: { (infoList) in
            self.infoList = infoList
            self.viewEmptyMessage.isHidden = self.infoList.count > 0
            self.canLoadMore = !(self.infoList.count >= self.totalRecord)
            self.tblInfo.reloadData()
        }) { (message) in
            print(message)
        }
    }
    
    fileprivate func searchInfo(searchText:String) {
        self.searchedInfoList = NSMutableArray.init()
        if searchText.count > 0 {
            for case let info as DBInfo in self.infoList {
                if ((info.title?.uppercased().contains(searchText.uppercased()))!) || ((info.title?.lowercased().contains(searchText.lowercased()))!) || ((info.desc?.uppercased().contains(searchText.uppercased()))!) || ((info.desc?.lowercased().contains(searchText.lowercased()))!) {
                    self.searchedInfoList?.add(info)
                }
            }
        }
        self.tblInfo.reloadData()
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
    
    @objc private func refresh(sender:AnyObject) {
        self.refreshControl.endRefreshing()
        self.currentPage = 0
        self.loadData()
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
    
    @IBAction func txtSearchEditingChanged(_ sender: Any) {
        if self.txtSearch.text != nil {
            let searchText = self.txtSearch.text!
            if searchText.count > 0 {
                self.searchInfo(searchText: searchText)
            } else {
                self.searchedInfoList = nil
                self.loadInfo()
            }
        }
    }
    
    @IBAction func btnSearchAction(_ sender: Any) {
        
    }
    
    @IBAction func btnProfileAction(_ sender: Any) {
        
    }
    
    @IBAction func btnMenuAction(_ sender: Any) {
        if(self.presentedViewController == nil){
            self.tabBarController?.tabBar.isHidden = true
            let viewController = UIStoryboard.init(name: Constants.Storyboard.SIDE_MENU, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.SIDE_MENU_VC) as! SideMenuVC
//            viewController.view.backgroundColor = #colorLiteral(red: 0.9882352941, green: 0.9882352941, blue: 0.9882352941, alpha: 0.17)
//            viewController.modalPresentationStyle = .overCurrentContext
//            viewController.modalTransitionStyle = .coverVertical
            viewController.modalPresentationStyle = .fullScreen
            viewController.delegate = self
            self.present(viewController, animated: false, completion: nil)
        } else {
            print("No need to present again")
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
        
        self.refreshControl.addTarget(self, action: #selector(self.refresh(sender:)), for: .valueChanged)
        self.tblInfo.addSubview(refreshControl)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        self.currentPage = 0
        self.loadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.LinkIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.LinkOut.rawValue)
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
    // MARK: UITableViewDataSource, UITableViewDelegate Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if self.searchedInfoList == nil {
            return self.infoList.count
        } else {
            return (self.searchedInfoList?.count)!
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var info = DBInfo.init()
        if self.searchedInfoList == nil {
            info = infoList[indexPath.row] as! DBInfo
        } else {
            info = searchedInfoList![indexPath.row] as! DBInfo
        }
        
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.INFO, for: indexPath) as! CellInfo
        cell.delegate = self
        cell.load(info: info)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        var info = DBInfo.init()
        if self.searchedInfoList == nil {
            info = infoList[indexPath.row] as! DBInfo
        } else {
            info = searchedInfoList![indexPath.row] as! DBInfo
        }
        let linksDetailVC = UIStoryboard.init(name: Constants.Storyboard.LINKS, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.Links_DETAIL_VC) as! LinksDetailVC
        linksDetailVC.info = info
        linksDetailVC.modalPresentationStyle = .overCurrentContext
        linksDetailVC.modalTransitionStyle = .coverVertical
        self.present(linksDetailVC, animated: false, completion: nil)
//        self.navigationController?.pushViewController(linksDetailVC, animated: false)
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if self.canLoadMore && indexPath.item == (self.infoList.count - 3) {
            self.loadData()
        }
    }
    
    // MARK: -
    // MARK: CellInfoDelegate Methods
    
    func btnLink(url: String) {
        let webViewVC = UIStoryboard.init(name: Constants.Storyboard.UTILITY, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.WEB_VIEW_VC) as! WebViewVC
        webViewVC.url = url
        print("Info URL: \(url)")
        self.navigationController?.pushViewController(webViewVC, animated: false)
    }
    
    func viewCallTapped(info: DBInfo) {
        do {
            var phoneNumber = info.contactNumber!
            phoneNumber = phoneNumber.components(separatedBy: " ").joined()
            let url = try self.getPhoneNumber(phoneNumber: phoneNumber)
                if #available(iOS 10.0, *) {
                    //iOS 10.0 and greater
                    UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
                } else {
                    //iOS 9
                    UIApplication.shared.openURL(url as URL)
                }
        } catch {
            self.view.makeToast("Phone number: \((info.contactNumber)!)", duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    func lblEmailTapped(info: DBInfo) {
        let mailComposeVC = self.configureMailController(email: info.emailAddress!)
        if MFMailComposeViewController.canSendMail() {
            self.present(mailComposeVC, animated: true, completion: nil)
        } else {
            showMailError(type: "email")
        }
    }
    
    // MARK: -
    // MARK: MFMailComposeViewControllerDelegate Methods
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: false, completion: nil)
    }
    
    // MARK: -
    // MARK: SideMenuVCDelegate Methods
    
    func close() {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func survey() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Survey.rawValue
    }
    
    func notification() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Notification.rawValue
    }
    
    func note() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Note.rawValue
    }
    
    func links() {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func feedback() {
        self.tabBarController?.tabBar.isHidden = false
        let viewController = UIStoryboard.init(name: Constants.Storyboard.FEEDBACK, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.FEEDBACK_VC) as! FeedbackVC
        self.navigationController?.pushViewController(viewController, animated: false)
    }
    
    func answer() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Answer.rawValue
    }
    
    func contactUs() {
        self.tabBarController?.tabBar.isHidden = false
        let viewController = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.CONTACT_US_VC) as! ContactUsVC
        viewController.modalTransitionStyle = .crossDissolve
        viewController.modalPresentationStyle = .fullScreen
        self.present(viewController, animated: false, completion: nil)
        
    }
    
    func privacyPolicy() {
        DispatchQueue.main.async {
            let dbResouceFile = DBResourceFileHandler.shared.getPrivacyPolicyResourceFile()
            let urlString = "\(URL_BASE)\(dbResouceFile.url ?? "")"
            if URL(string: urlString) != nil {
                self.moveToWebViewVC(header: dbResouceFile.desc ?? "Privacy Policy", url: urlString, pdfSource: "")
            } else { return }
        }
    }
    
    func about() {
        DispatchQueue.main.async {
            self.tabBarController?.tabBar.isHidden = false
            let dbResouceFile = DBResourceFileHandler.shared.getAboutUsResourceFile()
            let urlString = "\(URL_BASE)\(dbResouceFile.url ?? "")"
            if URL(string: urlString) != nil {
                self.moveToWebViewVC(header: dbResouceFile.desc ?? "About Us", url: urlString, pdfSource: "")
            } else { return }
        }
    }
    
    func changePassword() {
        self.tabBarController?.tabBar.isHidden = false
        let changePasswordVC = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.CHANGE_PASSWORD_VC) as! ChangePasswordVC
        changePasswordVC.modalPresentationStyle = .fullScreen
        self.present(changePasswordVC, animated: false, completion: nil)
    }
    
    func logOut() {
//        self.logout()
        let viewControllers: [UIViewController] = (self.tabBarController?.navigationController?.viewControllers)!
        print("ViewControllers: \(viewControllers)")
        for aViewController in viewControllers {
            if(aViewController is LoginVC){
                Util.clearDataBase()
                DispatchQueue.main.async {
                    self.tabBarController?.navigationController!.popToViewController(aViewController, animated: false);
                    
                }
            }
        }
    }

}
