//
//  AnswerVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import DropDown

class AnswerVC: UIViewController, UITableViewDataSource, UITableViewDelegate, SideMenuVCDelegate {
    
    @IBOutlet weak var lblRisccMessage: UILabel!
    @IBOutlet weak var btnMenu: UIButton!
    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var viewQuestionnaire: UIView!
    @IBOutlet weak var lblQuestionnaire: UILabel!
    @IBOutlet weak var imgDownArrow: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var viewEmptyMessage: UIView!
    @IBOutlet weak var lblEmptyMessage: UILabel!
    @IBOutlet weak var btnLink: UIButton!
    
    var answerList = NSMutableArray.init()
    var questionnaires = NSMutableArray.init()
    var selectedQuestionnaire = DBActiveQuestionnaire.init()
    var dropDown = DropDown()
    var refreshControl = UIRefreshControl()
    var moreInformation: String? = nil
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.btnMenu.setImage(UIImage.init(named: "ic_menu")?.imageWithColor(color: .white), for: .normal)
        self.lblHeader.text = NSLocalizedString("Your Answer", comment: "")
        self.lblEmptyMessage.text = NSLocalizedString("No answer available!", comment: "")
        self.lblRisccMessage.isHidden = true
        self.lblRisccMessage.textColor = .white
        self.lblRisccMessage.backgroundColor = UIColor(hexString: Constants.Color.COLOR_BASE)
        self.btnLink.isHidden = true
    }
    
    fileprivate func loadData() {
        self.loadGroupQuestionnaire()
    }
    
    fileprivate func loadAnsweredQuestion() {
        self.answerList = DBAnswerDataHandler.shared.getAnswers()
        self.viewEmptyMessage.isHidden = self.answerList.count > 0

        if self.answerList.count > 0 {
            self.loadRisccValue()
        }
        self.tableView.reloadData()
    }
    
    fileprivate func loadGroupQuestionnaire() {
        APIQuestionHandler.shared.requestGroupQuestionnaire { (message, questionnaires) in
            self.questionnaires = questionnaires
            print("Questionnaires: \(questionnaires.count)")
            if self.questionnaires.count > 0 {
                self.imgDownArrow.isHidden = self.questionnaires.count == 1 ? true : false
                let questionnaierActive = self.questionnaires[0] as! DBActiveQuestionnaire
                self.selectedQuestionnaire = questionnaierActive
                self.lblQuestionnaire.text = self.selectedQuestionnaire.title!
                self.loadAnswerByGroupQuestionnaire()
            } else {
                self.imgDownArrow.isHidden = true
            }
        } failure: { (message) in
            print(message)
        }
    }
    
    fileprivate func loadAnswerByGroupQuestionnaire() {
        self.showLoading(view: self.view, text: "")
        APIQuestionHandler.shared.requestAnswerByGroupQuestionnaier(groupQuestionnaireId: self.selectedQuestionnaire.questionId) { (message) in
            self.stopLoading(fromView: self.view)
            self.loadAnsweredQuestion()
        } failure: { (message) in
            self.stopLoading(fromView: self.view)
            print(message)
        }
    }
    
    fileprivate func loadRisccValue() {
        APIQuestionHandler.shared.requestRisccValue(for: self.selectedQuestionnaire.questionnaireId) { message, moreInfo in
            DispatchQueue.main.async {
                self.moreInformation = moreInfo
                self.lblRisccMessage.isHidden = false
                self.lblRisccMessage.text = message
                guard let moreInfo = self.moreInformation, moreInfo.count > 0 else {
                    self.btnLink.isHidden = true
                    return
                }
                self.btnLink.isHidden = false
            }
        } failure: { message in
            DispatchQueue.main.async {
                self.moreInformation = nil
                self.btnLink.isHidden = true
                self.lblRisccMessage.text = ""
            }
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
    
    private func dropDownView(view:UIView, options:[String]) {
        self.dropDown.anchorView = view
        self.dropDown.bottomOffset = CGPoint(x: 0, y: self.viewQuestionnaire.bounds.height)
        self.dropDown.cellHeight = self.viewQuestionnaire.bounds.height
        self.dropDown.dataSource = options
        self.dropDown.show()
        self.dropDown.selectionAction = {(index: Int, item: String) in
            
            if view == self.viewQuestionnaire {
                let questionnaire = self.questionnaires.object(at: index) as! DBActiveQuestionnaire
                if self.selectedQuestionnaire.questionId != questionnaire.questionId {
                    self.selectedQuestionnaire = questionnaire
                    self.lblQuestionnaire.text = item
                    self.loadAnswerByGroupQuestionnaire()
                }
            }
        }
    }
    
    @objc private func refresh(sender:AnyObject) {
        self.refreshControl.endRefreshing()
        self.loadData()
    }
    
    private func logout() {
        APIAnalyticsHandler.shared.postAppAnalytics(analyticsType: ANALYTICS_TYPE.UserOut.rawValue, success: { (message) in
            let viewControllers: [UIViewController] = (self.tabBarController?.navigationController?.viewControllers)!
            for aViewController in viewControllers {
                if(aViewController is LoginVC){
                    Util.clearDataBase()
                    self.tabBarController?.navigationController!.popToViewController(aViewController, animated: false);
                }
            }
        }, failure: { (message) in
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        })
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
    
    @IBAction func btnMenuAction(_ sender: Any) {
//        self.navigationController?.popViewController(animated: false)
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
    
    @IBAction func viewQuestionnaireAction(_ sender: Any) {
        let options: [String] = self.questionnaires.map({(($0 as? DBActiveQuestionnaire)?.title)!})
        self.dropDownView(view: self.viewQuestionnaire, options: options)
    }
    
    @IBAction func moreInformationAction(_ sender: Any) {
        guard let moreInfo = self.moreInformation, moreInfo.count > 0 else {
            debugPrint("No more information available.")
            return
        }
        if URL(string: moreInfo) != nil {
            self.moveToWebViewVC(header: "", url: moreInfo, pdfSource: "")
        } else { return }
        
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
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.refreshControl.addTarget(self, action: #selector(self.refresh(sender:)), for: .valueChanged)
        self.tableView.addSubview(refreshControl)
        
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        self.loadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.YourAnswerIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.YourAnswerOut.rawValue)
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
    // MARK: , UITableViewDataSource, UITableViewDelegate Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.answerList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let answer = self.answerList[indexPath.row] as! DBAnswerData
        let cell = tableView.dequeueReusableCell(withIdentifier: "CellAnsweredQuestion", for: indexPath) as! CellAnsweredQuestion
        cell.load(answer: answer)
        return cell
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
        self.tabBarController?.selectedIndex = TAB_INDEX.Link.rawValue
    }
    
    func feedback() {
        self.tabBarController?.tabBar.isHidden = false
        let viewController = UIStoryboard.init(name: Constants.Storyboard.FEEDBACK, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.FEEDBACK_VC) as! FeedbackVC
        self.navigationController?.pushViewController(viewController, animated: false)
    }
    
    func answer() {
        self.tabBarController?.tabBar.isHidden = false
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
//        self.navigationController?.pushViewController(changePasswordVC, animated: false)
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
