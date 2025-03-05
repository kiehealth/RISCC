//
//  TutorialVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD

class TutorialVC: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {

    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var btnSkip: UIButton!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var btnSkipWidth: NSLayoutConstraint!
    
    var isFromSetting: Bool = false
    var tutorialList = NSMutableArray.init()
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        
    }
    
    fileprivate func loadData() {
        self.tutorialList = Util.tutorialList()
        self.pageControl.currentPage = 0
        self.pageControl.numberOfPages = self.tutorialList.count
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
    
    @IBAction func btnSkipAction(_ sender: Any) {
        if self.isFromSetting {
            self.navigationController?.popViewController(animated: false)
        } else {
            let mainTabVC = UIStoryboard.init(name: Constants.Storyboard.MAIN_TAB, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.MAIN_TAB_VC) as! MainTabVC
            self.navigationController?.pushViewController(mainTabVC, animated: false)
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
        loadData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
//        if !DBSessionHandler.shared.isAppRinningFirstTime() && !self.isFromSetting {
//            self.btnSkipAction(self.btnSkip)
//        } else if DBSessionHandler.shared.isAppRinningFirstTime() {
//            let preference = DBPreference.init()
//            preference.isUnreadMessageNotificationEnabled = true
//            preference.commit()
//        }
        
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        
        let countryCode = Locale.current.languageCode
        if countryCode == "sv" {
            self.btnSkipWidth.constant = 100
        }
        
        self.tabBarController?.tabBar.isHidden = true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.TutorialIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.TutorialOut.rawValue)
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
    // MARK: UICollectionViewDelegate, UICollectionViewDataSource Methods
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.tutorialList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let tutorial = self.tutorialList[indexPath.row] as! TutorialModel
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.CellIdentifier.APP_TUTORIAL, for: indexPath) as! CellAppTutorial
        cell.load(tutorial: tutorial)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: self.collectionView.frame.width, height: self.collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        self.pageControl.currentPage = indexPath.row
//        if indexPath.row == (self.tutorialList.count - 1) {
//            self.btnSkip.setTitle("FINISH", for: .normal)
//        } else {
//            self.btnSkip.setTitle("SKIP", for: .normal)
//        }
    }

}
