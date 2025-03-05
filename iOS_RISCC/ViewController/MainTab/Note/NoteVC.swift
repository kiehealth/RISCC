//
//  NoteVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD

class NoteVC: UIViewController, UITableViewDataSource, UITableViewDelegate, CellNoteDelegate, SideMenuVCDelegate {

    @IBOutlet weak var btnMenu: UIButton!
    @IBOutlet weak var btnProfile: UIButton!
    @IBOutlet weak var tblNote: UITableView!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var headerViewHeight: NSLayoutConstraint!
    @IBOutlet weak var txtSearch: UITextField!
    @IBOutlet weak var lblEmptyMessage: UILabel!
    @IBOutlet weak var viewEmptyMessage: UIView!
    
    var noteList = NSMutableArray.init()
    var searchedNoteList: NSMutableArray? = nil
    var btnSearch = UIButton()
    var refreshControl = UIRefreshControl()
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
        self.lblEmptyMessage.text = NSLocalizedString("No note available!", comment: "")
    }
    
    fileprivate func loadData() {
        self.loadNotesAPI()
    }
    
    fileprivate func loadNotesAPI() {
        self.showLoading(view: self.view, text: "")
        APINoteHandler.shared.requestNotes(page: self.currentPage, sizePerPage: self.sizePerPage, success: { (message, totalRecord) in
            print(message)
            self.stopLoading(fromView: self.view)
            self.currentPage += 1
            self.totalRecord = totalRecord
            self.loadNotes()
        }) { (message) in
            self.stopLoading(fromView: self.view)
            print(message)
        }
    }
    
    fileprivate func loadNotes() {
        self.noteList = DBNoteHandler.shared.getNotes()
        self.viewEmptyMessage.isHidden = self.noteList.count > 0
        self.canLoadMore = !(self.noteList.count >= self.totalRecord)
        self.tblNote.reloadData()
    }
    
    fileprivate func searchNote(searchText: String) {
        self.searchedNoteList = NSMutableArray.init()
        if searchText.count > 0 {
            for case let note as DBNote in self.noteList {
                if ((note.title?.uppercased().contains(searchText.uppercased()))!) || ((note.title?.lowercased().contains(searchText.lowercased()))!) || ((note.desc?.uppercased().contains(searchText.uppercased()))!) || ((note.desc?.lowercased().contains(searchText.lowercased()))!) {
                    self.searchedNoteList!.add(note)
                }
            }
        }
        self.tblNote.reloadData()
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
    
    private func moveToNextVC(header:String, buttonTitle:String, note:DBNote?, isAddNote:Bool) {
        let noteAddEditVC = UIStoryboard.init(name: Constants.Storyboard.NOTE, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.NOTE_ADD_EDIT_VC) as! NoteAddEditVC
        noteAddEditVC.header = header
        noteAddEditVC.buttonTitle = buttonTitle
        noteAddEditVC.note = note
        noteAddEditVC.isAddNote = isAddNote
        self.navigationController?.pushViewController(noteAddEditVC, animated: false)
    }
    
    private func moveToDetailVC(note:DBNote) {
        let noteDetailVC = UIStoryboard.init(name: Constants.Storyboard.NOTE, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.NOTE_DETAIL_VC) as! NoteDetailVC
        noteDetailVC.note = note
        noteDetailVC.modalPresentationStyle = .overCurrentContext
        noteDetailVC.modalTransitionStyle = .crossDissolve
        self.present(noteDetailVC, animated: true, completion: nil)
    }
    
    @objc private func refresh(sender:AnyObject) {
        self.refreshControl.endRefreshing()
        self.currentPage = 0
        self.loadData()
    }
    
    @objc private func alertControllerBackgroundTapped() {
        self.dismiss(animated: true, completion: nil)
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
    
    @IBAction func btnAddAction(_ sender: Any) {
//        self.moveToNextVC(header: NSLocalizedString("Add New Note", comment: ""), buttonTitle: "Save Note", note: nil, isAddNote: true)
        self.moveToNextVC(header: NSLocalizedString("Note", comment: ""), buttonTitle: "Save", note: nil, isAddNote: true)
    }
    
    @IBAction func txtSearchEditingChanged(_ sender: Any) {
        if (self.txtSearch.text != nil) {
            let searchText: String = self.txtSearch.text!
            if searchText.count > 0 {
                self.searchNote(searchText: searchText)
            } else {
                self.searchedNoteList = nil
                self.loadNotes()
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
        self.loadData()
        
        self.refreshControl.addTarget(self, action: #selector(self.refresh(sender:)), for: .valueChanged)
        self.tblNote.addSubview(refreshControl)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Util.isIPhoneXOrAbove() {
            self.headerViewHeight.constant = HEADER_VIEW_HEIGHT
        }
        
        self.loadNotes()
        if DiaryHandler.shared.isNoteAddedOrUpdated {
            DiaryHandler.shared.isNoteAddedOrUpdated = false
            self.loadNotesAPI()
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NoteIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.NoteOut.rawValue)
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
        if self.searchedNoteList == nil {
            return self.noteList.count
        } else {
            return self.searchedNoteList!.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var dbNote = DBNote.init()
        if self.searchedNoteList == nil {
            dbNote = self.noteList[indexPath.row] as! DBNote
        } else {
            dbNote = self.searchedNoteList![indexPath.row] as! DBNote
        }
        let note = self.noteList[indexPath.row] as! DBNote
        let cell = tableView.dequeueReusableCell(withIdentifier: "CellNote", for: indexPath) as! CellNote
        cell.delegate = self
        cell.load(note: dbNote)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        var note = DBNote.init()
        if self.searchedNoteList == nil {
            note = self.noteList[indexPath.row] as! DBNote
        } else {
            note = self.searchedNoteList![indexPath.row] as! DBNote
        }
        self.moveToNextVC(header: NSLocalizedString("Edit Note", comment: ""), buttonTitle: "Edit Note", note: note, isAddNote: false)
//        self.moveToDetailVC(note: note)
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if self.canLoadMore && indexPath.item == (self.noteList.count - 3) {
            self.loadData()
        }
    }
    
    // MARK: -
    // MARK: CellNoteDelegate Methods
    
    func btnDeleteNote(note: DBNote) {
        let alert = UIAlertController(title: "", message: NSLocalizedString("Do you want to delete this note?", comment: ""), preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Delete", comment: ""), style: .default, handler: { (action) in
            if note.isSync {
                APINoteHandler.shared.deleteNote(noteId: note.noteId!, success: { (message) in
                    DBNoteHandler.shared.deleteNote(noteId: note.noteId!)
                    self.loadNotesAPI()
                }) { (message) in
                    print(message)
                }
            } else {
                DBNoteHandler.shared.deleteNote(noteId: note.noteId!)
                self.loadNotes()
            }
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: ""), style: .cancel, handler: nil))
        self.present(alert, animated: true) {
            alert.view.superview?.isUserInteractionEnabled = true
            alert.view.superview?.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.alertControllerBackgroundTapped)))
        }
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
            let langStr = Locale.current.languageCode
            var dbResouceFile = DBResourceFileHandler.shared.getPrivacyPolicyResourceFile()
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
