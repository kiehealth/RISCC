//
//  NotificationDetailVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD

class NotificationDetailVC: UIViewController {

    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    
    var notification = DBNotification.init()

    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        self.btnClose.layer.shadowColor = UIColor.lightGray.withAlphaComponent(0.5).cgColor
        self.btnClose.layer.shadowRadius = 4
        self.btnClose.layer.shadowOffset = CGSize(width: 0, height: 3)
        self.btnClose.layer.shadowOpacity = 0.6
        self.btnClose.layer.rasterizationScale = UIScreen.main.scale
        self.btnClose.layer.shouldRasterize = true
    }
    
    fileprivate func loadData() {
        self.lblTitle.text = self.notification.title
        self.lblDesc.text = self.notification.desc
//        self.lblDate.text = Util.dateToString_MMM_dd_yyyy(date: self.notification.date!)
        self.lblDate.text = Util.dateToString_MMM_dd_yyyy_hh_mm_a(date: self.notification.date!)
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
    
    @IBAction func btnCloseAction(_ sender: Any) {
//        self.navigationController?.popViewController(animated: false)
        self.dismiss(animated: false, completion: nil)
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
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NotificationDetailIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NotificationDetailOut.rawValue)
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
