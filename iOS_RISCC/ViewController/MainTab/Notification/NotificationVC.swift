//
//  NotificationVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD

class NotificationVC: UIViewController, UITableViewDataSource, UITableViewDelegate, CellNotificationDelegate, SideMenuVCDelegate {

    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var btnMenu: UIButton!
    @IBOutlet weak var tblNotification: UITableView!
    @IBOutlet weak var btnProfile: UIButton!
    @IBOutlet weak var txtSearch: DesignableUITextField!
    @IBOutlet weak var viewEmptyMessage: UIView!
    @IBOutlet weak var lblEmptyMessage: UILabel!
    
    var notifications = NSMutableArray.init()
    var refreshControl = UIRefreshControl()
    var searchNotificationList: NSMutableArray? = nil
    var currentPage = 0
    var totalRecord = 0
    var sizePerPage = 10
    var canLoadMore: Bool = false
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.viewEmptyMessage.isHidden = true
        self.btnMenu.setImage(UIImage.init(named: "ic_menu")?.imageWithColor(color: .white), for: .normal)
        self.lblEmptyMessage.text = NSLocalizedString("No notification available!", comment: "")
    }
    
    fileprivate func loadData() {
        self.loadNotificationAPI()
    }
    
    fileprivate func loadNotificationAPI() {
        self.showLoading(view: self.view, text: "")
        APINotificationHandler.shared.requestNotification(page: self.currentPage, sizePerPage: self.sizePerPage, success: { (message, totalRecord)  in
            self.stopLoading(fromView: self.view)
            self.currentPage += 1
            self.totalRecord = totalRecord
            self.loadNotification()
        }) { (message) in
            self.stopLoading(fromView: self.view)
            print(message)
        }
    }
    
    fileprivate func loadNotification() {
        DBNotificationHandler.shared.getNotifications(success: { (notifications) in
            self.notifications = notifications
            print("Notification: \(self.notifications.count)")
            self.viewEmptyMessage.isHidden = self.notifications.count > 0
            self.canLoadMore = !(self.notifications.count >= self.totalRecord)
            self.tblNotification.reloadData()
        }) { (message) in
            print(message)
        }
    }
    
    fileprivate func searchNotification(searchText: String) {
        self.searchNotificationList = NSMutableArray.init()
        if searchText.count > 0 {
            for case let notification as DBNotification in self.notifications {
                if ((notification.title?.uppercased().contains(searchText.uppercased()))!) || ((notification.title?.lowercased().contains(searchText.lowercased()))!) || ((notification.desc?.uppercased().contains(searchText.uppercased()))!) || ((notification.desc?.lowercased().contains(searchText.lowercased()))!) {
                    self.searchNotificationList!.add(notification)
                }
            }
        }
        self.tblNotification.reloadData()
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
    
    @objc private func refresh(sender:AnyObject) {
        self.refreshControl.endRefreshing()
        self.currentPage = 0
        self.loadData()
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
    
    @IBAction func btnProfileAction(_ sender: Any) {
        
    }
    
    @IBAction func txtSearchEditingChanged(_ sender: Any) {
        if (self.txtSearch.text != nil) {
            let searchText: String = self.txtSearch.text!
            if searchText.count > 0 {
                self.searchNotification(searchText: searchText)
            } else {
                self.searchNotificationList = nil
                self.loadNotification()
            }
        }
    }
    
    @IBAction func btnMenuAction(_ sender: Any) {
        if(self.presentedViewController == nil){
            self.tabBarController?.tabBar.isHidden = true
            let viewController = UIStoryboard.init(name: Constants.Storyboard.SIDE_MENU, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.SIDE_MENU_VC) as! SideMenuVC

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
        self.tblNotification.addSubview(refreshControl)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        self.currentPage = 0
        self.loadData()
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NotificationIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NotificationOut.rawValue)
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
        if self.searchNotificationList == nil {
            return self.notifications.count
        } else {
            return self.searchNotificationList!.count
        }
//        return self.notifications.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let notification = self.notifications[indexPath.row] as! DBNotification
        var notification = DBNotification()
        if self.searchNotificationList == nil {
            notification = self.notifications[indexPath.row] as! DBNotification
        } else {
            notification = self.searchNotificationList![indexPath.row] as! DBNotification
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.NOTIFICATION, for: indexPath) as! CellNotification
        cell.delegate = self
        cell.load(notification: notification)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let notification = self.notifications[indexPath.row] as! DBNotification
        print("You have selected")
        
        let notificationDetailVC = UIStoryboard.init(name: Constants.Storyboard.NOTIFICATION, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.NOTIFICATION_DETAIL_VC) as! NotificationDetailVC
        notificationDetailVC.notification = notification
        notificationDetailVC.modalPresentationStyle = .overCurrentContext
        notificationDetailVC.modalTransitionStyle = .coverVertical
        self.present(notificationDetailVC, animated: false, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if self.canLoadMore && indexPath.item == (self.notifications.count - 3) {
            self.loadData()
        }
    }
    
    // MARK: -
    // MARK: CellNotificationDelegate Methods
    
    func moveToNotificationDetail(notification: DBNotification) {
        DiaryHandler.shared.isFromNotification = true
        let notificationDetailVC = UIStoryboard.init(name: Constants.Storyboard.NOTIFICATION, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.NOTIFICATION_DETAIL_VC) as! NotificationDetailVC
        notificationDetailVC.notification = notification
        self.navigationController?.pushViewController(notificationDetailVC, animated: true)
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
    }
    
    func note() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Note.rawValue
    }
    
    func links() {
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.selectedIndex = TAB_INDEX.Link.rawValue
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
        let viewControllers: [UIViewController] = (self.tabBarController?.navigationController?.viewControllers)!
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
