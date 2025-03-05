//
//  SideMenuVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD

protocol SideMenuVCDelegate {
    func close()
    func survey()
    func notification()
    func note()
    func links()
    func feedback()
    func answer()
    func contactUs()
    func privacyPolicy()
    func about()
    func changePassword()
    func logOut()
}

class SideMenuVC: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var lblBuildVersion: UILabel!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var imgEditProfile: UIImageView!
    @IBOutlet weak var lblEditProfile: UILabel!
    @IBOutlet weak var viewEditProfile: UIView!
    
    
    var sideMenuList = NSMutableArray.init()
    var delegate:SideMenuVCDelegate?
    var loggedUser: DBUser? = nil
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.btnClose.setImage(UIImage.init(named: "ic_cancel")?.imageWithColor(color: .white), for: .normal)
        self.imgEditProfile.image = self.imgEditProfile.image?.imageWithColor(color: .white)
        self.lblEditProfile.text = NSLocalizedString("Edit Profile", comment: "")
        self.blurView()
        
        self.viewEditProfile.layer.borderColor = UIColor.white.cgColor
        
        self.btnClose.layer.shadowColor = UIColor.lightGray.withAlphaComponent(0.5).cgColor
        self.btnClose.layer.shadowRadius = 4
        self.btnClose.layer.shadowOffset = CGSize(width: 0, height: 3)
        self.btnClose.layer.shadowOpacity = 0.6
        self.btnClose.layer.rasterizationScale = UIScreen.main.scale
        self.btnClose.layer.shouldRasterize = true
    }
    
    fileprivate func loadData() {
        self.sideMenuList = Util.sideMenuList()
        let appVersion = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as? String
        self.lblBuildVersion.text = "\(NSLocalizedString("Build version", comment: "")) \(appVersion!)"
        self.loadUser()
    }
    
    fileprivate func blurView(){
        let blurEffect = UIBlurEffect(style: .light)
        let blurEffectView = UIVisualEffectView(effect: blurEffect)
        blurEffectView.frame = self.view.frame

        self.view.insertSubview(blurEffectView, at: 0)
    }
    
    fileprivate func loadUser() {
        self.loggedUser = DBUserHandler.shared.getLoggedUser()
        self.lblName.text = "\((self.loggedUser?.firstName!)!) \((self.loggedUser?.lastName!)!)"
        self.lblPhone.text = self.loggedUser?.mobileNumber!
        self.lblEmail.text = self.loggedUser?.email!
        if self.loggedUser?.imageUrl != nil {
            let imageURL = "\(URL_BASE)\((self.loggedUser?.imageUrl)!)"
            let url = URL(string: imageURL)
            let imageRequest = URLRequest.init(url: url!)
            self.imgProfile.af.setImage(withURLRequest: imageRequest, placeholderImage: UIImage.init(named: "userPlaceHolder"))
        } else  {
            self.imgProfile.image = UIImage.init(named: "userPlaceHolder")
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
    
    @objc private func alertControllerBackgroundTapped() {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    // MARK: -
    // MARK: Public Utility Methods
    
    
    // MARK: -
    // MARK: IBAction Methods Methods
    
    @IBAction func touchDown(_ sender: Any) {
        view.endEditing(true)
    }
    
    @IBAction func btnClose(_ sender: Any) {
        self.delegate?.close()
        dismiss(animated: false, completion: nil)
    }
    
    @IBAction func viewEditProfileAction(_ sender: Any) {
        let viewController = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.PROFILE_VC) as! ProfileVC
        viewController.modalPresentationStyle = .fullScreen
        self.present(viewController, animated: false, completion: nil)
    }
    
//    @IBAction func viewEditProfileAction(_ sender: Any) {
        
//    }
    
    @IBAction func btnLogoutAction(_ sender: Any) {
        let alert = UIAlertController(title: "", message: NSLocalizedString("Do you want to log out?", comment: ""), preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Log Out", comment: ""), style: .default, handler: { (action) in
            APIUserHandler.shared.requestLogout { (message) in
                self.dismiss(animated: false) {
                    self.delegate?.logOut()
                }
            } failure: { (message) in
                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
            }
//            APIAnalyticsHandler.shared.postAppAnalytics(analyticsType: ANALYTICS_TYPE.UserOut.rawValue, success: { (message) in
//                self.dismiss(animated: false) {
//                    self.delegate?.logOut()
//                }
//            }, failure: { (message) in
//                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
//            })
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: ""), style: .cancel, handler: nil))
        self.present(alert, animated: true) {
            alert.view.superview?.isUserInteractionEnabled = true
            alert.view.superview?.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.alertControllerBackgroundTapped)))
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
        self.loadUser()
//        DispatchQueue.main.async {
//
//            self.tabBarController?.tabBar.isHidden = true
//            self.tabBarController?.tabBar.layer.zPosition = -1
//        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.MenuIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.tabBarController?.tabBar.isHidden = false
        self.tabBarController?.tabBar.layer.zPosition = 0
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.MenuOut.rawValue)
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
    // MARK: UITableViewDataSource, UITableViewDelegate Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.sideMenuList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let sideMenu = self.sideMenuList[indexPath.row] as! SideMenu
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.CellIdentifier.SIDE_MENU, for: indexPath) as! CellSideMenu
        cell.load(sideMenu: sideMenu)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let sideMenu = self.sideMenuList[indexPath.row] as! SideMenu
        if sideMenu.viewController == Constants.StoryboardId.SURVEY_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.survey()
            
        } else if sideMenu.viewController == Constants.StoryboardId.NOTIFICATION_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.notification()
            
        } else if sideMenu.viewController == Constants.StoryboardId.NOTE_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.note()
            
        } else if sideMenu.viewController == Constants.StoryboardId.LINKS_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.links()
            
        } else if sideMenu.viewController == Constants.StoryboardId.FEEDBACK_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.feedback()
            
        } else if sideMenu.viewController == Constants.StoryboardId.ANSWERED_QUESTION_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.answer()
        } else if sideMenu.viewController == Constants.StoryboardId.CONTACT_US_VC {
//            self.dismiss(animated: false, completion: nil)
//            self.delegate?.contactUs()
            let viewController = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.CONTACT_US_VC) as! ContactUsVC
            viewController.modalTransitionStyle = .crossDissolve
            viewController.modalPresentationStyle = .overCurrentContext
            self.present(viewController, animated: false, completion: nil)
            
        } else if sideMenu.title == "Privacy Policy" || sideMenu.title == "Integritetspolicy" {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.privacyPolicy()
            
        } else if sideMenu.title == "About" || sideMenu.title == "Om...." {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.about()
            
        } else if sideMenu.viewController == Constants.StoryboardId.CHANGE_PASSWORD_VC {
            self.dismiss(animated: false, completion: nil)
            self.delegate?.changePassword()
            
        } else if sideMenu.viewController == Constants.StoryboardId.LOGIN_VC {
            let alert = UIAlertController(title: "", message: NSLocalizedString("Do you want to log out?", comment: ""), preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Log Out", comment: ""), style: .default, handler: { (action) in
                APIUserHandler.shared.requestLogout { (message) in
                    self.dismiss(animated: false) {
                        self.delegate?.logOut()
                    }
                } failure: { (message) in
                    self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
                }
    //            APIAnalyticsHandler.shared.postAppAnalytics(analyticsType: ANALYTICS_TYPE.UserOut.rawValue, success: { (message) in
    //                self.dismiss(animated: false) {
    //                    self.delegate?.logOut()
    //                }
    //            }, failure: { (message) in
    //                self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
    //            })
            }))
            alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: ""), style: .cancel, handler: nil))
            self.present(alert, animated: true) {
                alert.view.superview?.isUserInteractionEnabled = true
                alert.view.superview?.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.alertControllerBackgroundTapped)))
            }
            
        }
    }
}
