//
//  ProfileVC.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import MBProgressHUD
import DropDown
import Toast_Swift
import AlamofireImage

class ProfileVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var btnDeleteProfile: UIButton!
    @IBOutlet weak var btnCamera: UIButton!
    @IBOutlet weak var viewImgProfile: UIView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var txtFirstName: UITextField!
    @IBOutlet weak var txtLastName: UITextField!
    @IBOutlet weak var txtPhone: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var btnEditUpdate: UIButton!
    @IBOutlet weak var btnCancel: UIButton!
    @IBOutlet weak var btnEdit: UIButton!
    @IBOutlet weak var viewEdit: UIView!
    @IBOutlet weak var viewProfileDetail: UIView!
    @IBOutlet weak var lblLocalizeFirstName: UILabel!
    @IBOutlet weak var lblLocalizeLastName: UILabel!
    @IBOutlet weak var lblLocalizePhone: UILabel!
    @IBOutlet weak var lblLocalizeEmail: UILabel!
    
    var loggedUser: DBUser? = nil
    var maxDate = Calendar.current.date(byAdding: .year, value: -5, to: Date())
    var dateOfBirth = NSDate.init()
    var verificationId: String? = nil
    var txtVerificationCode: UITextField?
    var dropDown = DropDown()
    var isEditable: Bool = false
    var image: UIImage? = nil
    var textFields: [UITextField] = []
    
    // MARK: -
    // MARK: Private Utility Methods
    
    
    fileprivate func configureView(){
        //        self.btnBack.setImage(UIImage.init(named: "back")?.imageWithColor(color: UIColor.init(hexString: Constants.Color.BUTTON_GRAY)), for: .normal)
        
        self.viewEdit.isHidden = true
        self.btnCamera.layer.borderColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE).cgColor
        self.btnDeleteProfile.layer.borderColor = Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE).cgColor
        self.btnEdit.setImage(UIImage.init(named: "edit")?.imageWithColor(color: Util.hexStringToUIColor(hex: Constants.Color.COLOR_BASE)), for: .normal)
        self.isProfileEditable(isEditable: false)
        self.textFields = [self.txtFirstName, self.txtLastName, self.txtPhone, self.txtEmail]
        self.textFields.forEach { (textField) in
            textField.layer.addBorder(edge: .bottom, color: Util.hexStringToUIColor(hex: Constants.Color.COLOR_BORDER), thickness: 1.0)
        }
        self.lblLocalizeFirstName.text = NSLocalizedString("First Name", comment: "")
        self.lblLocalizeLastName.text = NSLocalizedString("Last Name", comment: "")
        self.lblLocalizePhone.text = NSLocalizedString("Phone", comment: "")
        self.lblLocalizeEmail.text = NSLocalizedString("Email", comment: "")
    }
    
    fileprivate func loadData() {
        self.btnEditAction(self.btnEdit!)
        self.loadUser()
    }
    
    fileprivate func loadUser() {
        self.loggedUser = DBUserHandler.shared.getLoggedUser()
        self.txtFirstName.text = self.loggedUser?.firstName!
        self.txtLastName.text = self.loggedUser?.lastName!
        self.lblName.text = "\(self.txtFirstName.text!) \(self.txtLastName.text!)"
        self.txtPhone.text = self.loggedUser?.mobileNumber!
        self.lblPhone.text = self.txtPhone.text!
        self.txtEmail.text = self.loggedUser?.email!
        self.lblEmail.text = self.txtEmail.text!

        if self.loggedUser?.imageUrl != nil {
            let imageURL = "\(URL_BASE)\((self.loggedUser?.imageUrl)!)"
            print("ImageURL: \(imageURL)")
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
    
    private func getImagePickerActionSheet(){
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        actionSheet.addAction(UIAlertAction(title:"Camera", style: .default, handler: { (action: UIAlertAction) in
            let imagePickerController = UIImagePickerController()
            if UIImagePickerController.isSourceTypeAvailable(.camera){
                imagePickerController.delegate = self
                imagePickerController.sourceType = .camera
                self.present(imagePickerController, animated: true, completion: nil)
            } else {
                //                print("Camera not available")
            }
        }))
        actionSheet.addAction(UIAlertAction(title:"PhotoLibrary", style: .default, handler: { (action: UIAlertAction) in
            let imagePickerController = UIImagePickerController()
            imagePickerController.delegate = self
            imagePickerController.sourceType = .photoLibrary
            self.present(imagePickerController, animated: true, completion: nil)
        }))
        actionSheet.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        self.present(actionSheet, animated: true, completion: nil)
    }
        
    private func isProfileEditable(isEditable:Bool) {
        self.viewProfileDetail.isHidden = isEditable
        self.viewEdit.isHidden = isEditable ? false : true
        self.btnEditUpdate.setTitle(isEditable ? NSLocalizedString("UPDATE", comment: "") : NSLocalizedString("EDIT", comment: ""), for: .normal)
        self.btnCamera.isHidden = isEditable ? false : true
        self.btnDeleteProfile.isHidden = isEditable ? false : true
        self.txtFirstName.isUserInteractionEnabled = isEditable ? true : false
        self.txtLastName.isUserInteractionEnabled = isEditable ? true : false
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
    
    @IBAction func btnBackAction(_ sender: Any) {
        //        self.navigationController?.popViewController(animated: false)
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func btnEditAction(_ sender: Any) {
        self.isEditable = true
        self.isProfileEditable(isEditable: self.isEditable)
    }
    
    @IBAction func btnCameraAction(_ sender: Any) {
        self.getImagePickerActionSheet()
    }
    
    @IBAction func btnEditUpdateAction(_ sender: Any) {
        let postParameter = NSMutableDictionary.init()
        postParameter.setValue(self.txtPhone.text!, forKey: "mobileNumber")
        postParameter.setValue(DEVICE_IOS, forKey: "devicePlatform")
        postParameter.setValue(DiaryHandler.shared.deviceToken, forKey: "deviceToken")
        postParameter.setValue(PUSH_NOTIFICATION_MODE, forKey: "iosNotificationMode")
        postParameter.setValue(self.txtEmail.text!, forKey: "emailAddress")
        postParameter.setValue(self.txtFirstName.text!, forKey: "firstName")
        postParameter.setValue(self.txtLastName.text!, forKey: "lastName")
        if let resizedImage = self.image?.imageWithSize(size: CGSize(width: 100, height: 100))  {
            postParameter.setValue(resizedImage.base64EncodedString(), forKey: "image")
        }
        self.showLoading(view: self.view, text: "")
        APIUserHandler.shared.updateUser(postParameter: postParameter, success: { (message) in
            self.stopLoading(fromView: self.view)
            self.loadUser()
            self.btnBackAction(self.btnBack!)
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }) { (message) in
            self.stopLoading(fromView: self.view)
            self.view.makeToast(message, duration: Constants.Timer.TOAST_DURATION, position: .center)
        }
    }
    
    @IBAction func btnCancelAction(_ sender: Any) {
        self.isEditable = false
        self.isProfileEditable(isEditable: self.isEditable)
    }
    
    @IBAction func imgProfileAction(_ sender: Any) {
        let imageViewVC = UIStoryboard.init(name: Constants.Storyboard.SETTING, bundle: nil).instantiateViewController(withIdentifier: Constants.StoryboardId.IMAGE_VIEW_VC) as! ImageViewVC
        imageViewVC.image = self.imgProfile.image
        self.present(imageViewVC, animated: false) {
            //
        }
    }
    
    @IBAction func btnDeleteProfileAction(_ sender: Any) {
        self.imgProfile.image = UIImage(named: "userPlaceHolder")
        self.image = self.imgProfile.image
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
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.ProfileIn.rawValue)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.ProfileOut.rawValue)
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
    // MARK: UIImagePickerControllerDelegate, UINavigationControllerDelegate Methods
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        self.image = info[.originalImage] as? UIImage
        self.imgProfile.image = self.image
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
}
