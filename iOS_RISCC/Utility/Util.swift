//
//  Util.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import Foundation
import SystemConfiguration
import UIKit
import MapKit
import SharkORM
import Alamofire


class Util: NSObject {
    class func isValidPhoneNumber(phoneNumber: String) -> Bool {
//        let PHONE_REGEX = "^\\d{10}$" //For Nepal
        let PHONE_REGEX = "^\\d{8,10}$"
        let phoneTest = NSPredicate(format: "SELF MATCHES %@", PHONE_REGEX)
        let result =  phoneTest.evaluate(with: phoneNumber)
        return result
    }
    
    class func validateEmail(enteredEmail:String) -> Bool {
        let emailFormat = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format:"SELF MATCHES %@", emailFormat)
        return emailPredicate.evaluate(with: enteredEmail)
    }
    
    class func validateLink(link:String) -> Bool {
        if let url = NSURL(string: link) { // check if your application can open the NSURL instance
            return UIApplication.shared.canOpenURL(url as URL)
        }
        return false
    }
    
    class func decodeBase64(input:String) -> String {
        let decodedData = Data(base64Encoded: input)!
        let decodedString = String(data: decodedData, encoding: .utf8)!
        return decodedString
    }
    
    class func showAlertView(title: String, message:String, reference: Any) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
        (reference as AnyObject).present(alert, animated: true, completion: nil)
    }
    
    class func isNetworkAvailable() -> Bool {
        var zeroAddress = sockaddr_in()
        zeroAddress.sin_len = UInt8(MemoryLayout<sockaddr_in>.size)
        zeroAddress.sin_family = sa_family_t(AF_INET)
        
        guard let defaultRouteReachability = withUnsafePointer(to: &zeroAddress, {
            $0.withMemoryRebound(to: sockaddr.self, capacity: 1) {
                SCNetworkReachabilityCreateWithAddress(nil, $0)
            }
        }) else {
            return false
        }
        
        var flags: SCNetworkReachabilityFlags = []
        if !SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags) {
            return false
        }
        
        let isReachable = flags.contains(.reachable)
        let needsConnection = flags.contains(.connectionRequired)
        return (isReachable && !needsConnection)
    }
    
    class func getUUID() -> String{
        let deviceId = UIDevice.current.identifierForVendor?.uuidString
        return deviceId!
    }
    
    // MARK: -
    // MARK: Date Utility Methods
    
    class func unixTimeFromNSDate(date:NSDate) -> Int64{
        return (date as Date).millisecondsSince1970
    }

    class func dateFromUnixTime(unixTimeInterval:Double) -> NSDate{
        return NSDate(timeIntervalSince1970: (unixTimeInterval/1000));
    }
    
    
    class func dateToString_hour_minute_ampm(date:Date)->String{
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm a"
        return formatter.string(from: date as Date)
    }
    
    class func dateToString_hour_minute(date:Date)->String{
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm"
        return formatter.string(from: date as Date)
    }
    
    //    class func dateToString_yyy_MM_dd(date:NSDate) -> String{
    //        let dateFormatter = DateFormatter()
    //        dateFormatter.dateFormat = "yyyy-MM-dd"
    //        return dateFormatter.string(from: date as Date)
    //    }
    
    class func dateToString_dd_MMM(date:NSDate) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd, MMM"
        return dateFormatter.string(from: date as Date)
    }
    
    class func dateToString_MM_dd_yyyy(date:NSDate) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MM/dd/yyyy"
        return dateFormatter.string(from: date as Date)
    }
    
    class func dateToString_MMM_dd_yyyy(date:NSDate) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMM d, yyyy"
        return dateFormatter.string(from: date as Date)
    }
    
    class func dateToString_yyyy_MM_dd(date:NSDate) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.string(from: date as Date)
    }
    
    class func dateToString_MMM_dd_yyyy_hh_mm_a(date:NSDate) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMM dd, yyyy hh:mm a"
        return dateFormatter.string(from: date as Date)
    }
    
    class func dateToString_hh_mm_a_MM_dd_yyyy(date:NSDate) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "hh:mm a, MM/dd/yyyy"
        return dateFormatter.string(from: date as Date)
    }
    
    /*class func stringToDate_yyyy_MM_dd_Hour_minute(date:String)->Date{
     let dateFormatter = DateFormatter()
     dateFormatter.dateFormat = "yyyy-MM-dd HH:mm"
     return dateFormatter.date(from: date)!
     }*/
    
    class func currentDateMonth(date:Date) -> (year: Int, month: Int, day: Int, dayName: String, daysInMonth: Int, hour: Int, minute: Int) {
        let calendar = Calendar.current
        let year = calendar.component(.year, from: date)
        let month = calendar.component(.month, from: date)
        let day = calendar.component(.day, from: date)
        let hour = calendar.component(.hour, from: date)
        let minute = calendar.component(.minute, from: date)
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "EEEE"
        let dayName = dateFormatter.string(from: date).capitalized
        
        let range = calendar.range(of: .day, in: .month, for: date)!
        let numDays = range.count
        return (year, month, day, dayName, numDays, hour, minute)
    }
    
    class func convertToUTCAndGetCurretDate(date: Date) -> (year: Int, month: Int, day: Int, dayName: String, daysInMonth: Int, hour: Int, minute: Int) {
        var calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        calendar.timeZone = TimeZone(abbreviation: "UTC")!
        let year = calendar.component(.year, from: date)
        let month = calendar.component(.month, from: date)
        let day = calendar.component(.day, from: date)
        let hour = calendar.component(.hour, from: date)
        let minute = calendar.component(.minute, from: date)
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "EEEE"
        let dayName = dateFormatter.string(from: date).capitalized
        
        let range = calendar.range(of: .day, in: .month, for: date)!
        let numDays = range.count
        return (year, month, day, dayName, numDays, hour, minute)
        
    }
    
    class func addHourInCurrentDate(date:Date, minute:Int) -> Date {
        let calendar = Calendar.current
        let addedHourDate = calendar.date(byAdding: .minute, value: minute, to: date)
        //        let date = startDate.addingTimeInterval(hour * 60.0 * 60.0)
        return addedHourDate!
    }
    
    class func setDayOfMonthInDate(date: Date, day: Int) -> Date {
        let calendar: Calendar = Calendar.current
        var components: DateComponents = calendar.dateComponents([.year, .month, .day, .hour, .minute, .second], from: date)
        components.setValue(day, for: .day)
        let newDate = calendar.date(from: components)!
        return newDate
    }
    
    
    class func dayName(date: Date, ofDay: Int) -> String {
        let calendar: Calendar = Calendar.current
        var components: DateComponents = calendar.dateComponents([.year, .month, .day, .hour, .minute, .second], from: date)
        components.setValue(ofDay, for: .day)
        let date = calendar.date(from: components)!
        
        let dateFormatter = DateFormatter()
        //        dateFormatter.dateFormat = "EEEE"
        dateFormatter.dateFormat = "EE"
        return dateFormatter.string(from: date).capitalized
    }
    
    
    class func currentMonthStringToInt(monthString:String) -> Int{
        let calendar = NSCalendar(identifier: NSCalendar.Identifier.gregorian)
        let formatter = DateFormatter()
        formatter.dateFormat = "MMMM"
        let aDate = formatter.date(from: monthString)
        let monthInt = calendar!.component(.month , from: aDate!)
        return monthInt
    }
    
    class func currentMonthIntToString(monthInt:Int) -> String{
        let monthName = DateFormatter().monthSymbols[monthInt - 1]
        return monthName
    }
    
    class func calculateAgeFrom(birthDate: String) -> Int {
        let dateFormater = DateFormatter()
        dateFormater.dateFormat = "MM/dd/yyyy"
        let birthdayDate = dateFormater.date(from: birthDate)
        let calendar: NSCalendar! = NSCalendar(calendarIdentifier: .gregorian)
        let now = Date()
        let calcAge = calendar.components(.year, from: birthdayDate!, to: now, options: [])
        let age = calcAge.year
        return age!
    }
    
    class func isLaterDate(inputDate:NSDate)->Bool {
        let currentDate = NSDate()
        let laterDate: Bool = (currentDate.compare(inputDate as Date) == .orderedAscending)
        return laterDate
    }
    
    class func isEarlierDate(inputDate:NSDate)->Bool {
        let currentDate = NSDate()
        let earlierDate: Bool = (currentDate.compare(inputDate as Date) == .orderedDescending)
        return earlierDate
    }
    
    class func dateDifference(earlierDate:NSDate, laterDate: NSDate)->Int64 {
        let timeInterval = laterDate.timeIntervalSince(earlierDate as Date)  //In sconds
        return Int64(timeInterval)
    }
    
    class func secondsToHoursMinutesSeconds (seconds : Int64) -> (hour:Int64, minute:Int64, second:Int64) {
        return (seconds / 3600, (seconds % 3600) / 60, (seconds % 3600) % 60)
    }
    
    class func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        if ((cString.count) != 6) {
            return UIColor.gray
        }
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    
    class  func heightForView(text:String, font:UIFont, width:CGFloat) -> CGFloat{
        let label:UILabel = UILabel(frame: CGRect(x: 0, y: 0, width: width, height: CGFloat.greatestFiniteMagnitude))
        label.numberOfLines = 0
        label.lineBreakMode = NSLineBreakMode.byTruncatingTail
        label.font = font
        label.text = text
        label.sizeToFit()
        return label.frame.height
    }
    
    class func nullToNil(value : AnyObject?) -> AnyObject? {
        if value is NSNull {
            return nil
        } else {
            return value
        }
    }
    
    class func extractYouTubeId(from url: String) -> String? {
        let typePattern = "(?:(?:\\.be\\/|embed\\/|v\\/|\\?v=|\\&v=|\\/videos\\/)|(?:[\\w+]+#\\w\\/\\w(?:\\/[\\w]+)?\\/\\w\\/))([\\w-_]+)"
        let regex = try? NSRegularExpression(pattern: typePattern, options: .caseInsensitive)
        return regex
            .flatMap { $0.firstMatch(in: url, range: NSMakeRange(0, url.count)) }
            .flatMap { Range($0.range(at: 1), in: url) }
            .map { String(url[$0]) }
    }
    
    class func parseMapAddress(selectedItem:MKPlacemark) -> String {
        // put a space between "4" and "Melrose Place"
        let firstSpace = (selectedItem.subThoroughfare != nil && selectedItem.thoroughfare != nil) ? " " : ""
        // put a comma between street and city/state
        let comma = (selectedItem.subThoroughfare != nil || selectedItem.thoroughfare != nil) && (selectedItem.subAdministrativeArea != nil || selectedItem.administrativeArea != nil) ? ", " : ""
        // put a space between "Washington" and "DC"
        let secondSpace = (selectedItem.subAdministrativeArea != nil && selectedItem.administrativeArea != nil) ? " " : ""
        let addressLine = String(
            format:"%@%@%@%@%@%@%@",
            // street number
            selectedItem.subThoroughfare ?? "",
            firstSpace,
            // street name
            selectedItem.thoroughfare ?? "",
            comma,
            // city
            selectedItem.locality ?? "",
            secondSpace,
            // state
            selectedItem.administrativeArea ?? ""
        )
        return addressLine
    }
    
    class func leftView() -> UIView {
        let addSpace = "  "
        let lblSpace = UILabel.init()
        lblSpace.text = addSpace
        lblSpace.font = UIFont.init(name: "Lato", size: 14)
        lblSpace.textAlignment = .right
        lblSpace.sizeToFit()
        lblSpace.textColor = UIColor.black
        return lblSpace
    }
    
    class func textField() -> UITextField {
        let textField: UITextField = UITextField.init()
        textField.leftViewMode = UITextField.ViewMode.always
        textField.leftView = Util.leftView()
        return textField
    }
    
    class func shareView(view:UIView, item:[String])-> UIActivityViewController {
        let activityVC = UIActivityViewController.init(activityItems: item, applicationActivities: [])
        activityVC.popoverPresentationController?.sourceView = view
        return activityVC
    }
    
    class func getHoursMinutesSecondsFrom(seconds: Double) -> (hours: Int, minutes: Int, seconds: Int) {
        guard !(seconds.isNaN || seconds.isInfinite) else {
            return (0, 0, 0)// or do some error handling
        }
        let secs = Int(seconds)
        let hours = secs / 3600
        let minutes = (secs % 3600) / 60
        let seconds = (secs % 3600) % 60
        return (hours, minutes, seconds)
    }
    
    class func formatTimeFor(seconds: Double) -> String {
        guard !(seconds.isNaN || seconds.isInfinite) else {
            return "0:00"
        }
        
        let result = Util.getHoursMinutesSecondsFrom(seconds: seconds)
        let hoursString = "\(result.hours)"
        var minutesString = "\(result.minutes)"
        if minutesString.count == 1 {
            minutesString = "0\(result.minutes)"
        }
        var secondsString = "\(result.seconds)"
        if secondsString.count == 1 {
            secondsString = "0\(result.seconds)"
        }
        var time = "\(hoursString):"
        if result.hours >= 1 {
            time.append("\(minutesString):\(secondsString)")
        }
        else {
            time = "\(minutesString):\(secondsString)"
        }
        return time
    }
    
    class func containsSpecialCharacters(text:String) -> Bool {
        let characterset = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ")
        if text.rangeOfCharacter(from: characterset.inverted) != nil {
            return true
        } else {
            return false
        }
    }
    
    class func imageForTextFieldView(imageName:String, imageColor:UIColor) -> UIButton {
        let image = UIImage.init(named: imageName)?.withRenderingMode(.alwaysTemplate)
        let button = UIButton()
        button.tintColor = imageColor
        button.setImage(image, for: .normal)
        button.frame = CGRect(x: 0, y: 0, width: 30, height: 30)
        button.contentMode = .scaleAspectFit
        return button
    }
    
    class func isIPhoneXOrAbove() -> Bool {
        var status: Bool = false
        if UIDevice().userInterfaceIdiom == .phone {
            print("Screen Height: \(UIScreen.main.nativeBounds.height)")
            switch UIScreen.main.nativeBounds.height {
            case 1136:
                //                print("iPhone 5 or 5S or 5C")
                status = false
            case 1334:
                //                print("iPhone 6/6S/7/8")
                status = false
            case 1920, 2208:
                //                print("iPhone 6+/6S+/7+/8+")
                status = false
            case 2436:
                //                print("iPhone X, Xs")
                status = true
            case 2688:
                //                print("iPhone Xs Max")
                status = true
            case 1792:
                //                print("iPhone Xr")
                status = true
                
            case 2532:
                //                priint("iPhone 12")
                status = true
                
            default:
                //                print("unknown")
                status = true
            }
        }
        return status
    }
    
    class func isIphoneFive() -> Bool {
        var status: Bool = false
        if UIDevice().userInterfaceIdiom == .phone {
            switch UIScreen.main.nativeBounds.height {
            case 1136:
                //                print("iPhone 5 or 5S or 5C")
                status = true
            default:
                //                print("unknown")
                status = false
            }
        }
        return status
    }
    
    class func setUnderlineToString(text:String, color:UIColor) -> NSMutableAttributedString {
        let attributedString = [NSAttributedString.Key.foregroundColor : color, NSAttributedString.Key.underlineStyle : NSUnderlineStyle.single.rawValue] as [NSAttributedString.Key : Any]
        return NSMutableAttributedString(string: text, attributes: attributedString)
    }
    
    class func getHeader()-> HTTPHeaders? {
        let langStr = Locale.current.languageCode
        var languageCode = "EN"
        if langStr == "en" {
            languageCode = "EN"
        } else {
            languageCode = "SV"
        }
        if let user = DBUserHandler.shared.getLoggedUser(){
            let headers = ["Authorization": user.token!, "language": languageCode]
            print("Headers: \(headers)")
            return HTTPHeaders(headers)
        } else {
            let headers = ["language": languageCode]
            print("Headers: \(headers)")
            return HTTPHeaders(headers)
        }
    }
    
    
    class func tutorialList() -> NSMutableArray {
        let tutorialList = NSMutableArray.init()
        
        let tutorialOne = TutorialModel.init(title: "", image: "1_tutorial", desc: "")
        let tutorialTwo = TutorialModel.init(title: "", image: "2_tutorial", desc: "")
        let tutorialThree = TutorialModel.init(title: "", image: "3_tutorial", desc: "")
        let tutorialFour = TutorialModel.init(title: "", image: "4_tutorial", desc: "")
        let tutorialFive = TutorialModel.init(title: "", image: "5_tutorial", desc: "")
        let tutorialSix = TutorialModel.init(title: "", image: "6_tutorial", desc: "")
        let tutorialSeven = TutorialModel.init(title: "", image: "7_tutorial", desc: "")
        let tutorialEight = TutorialModel.init(title: "", image: "8_tutorial", desc: "")
        
        tutorialList.add(tutorialOne)
        tutorialList.add(tutorialTwo)
        tutorialList.add(tutorialThree)
        tutorialList.add(tutorialFour)
        tutorialList.add(tutorialFive)
        tutorialList.add(tutorialSix)
        tutorialList.add(tutorialSeven)
        tutorialList.add(tutorialEight)
        
        return tutorialList
    }
    
    class func languageList() -> NSMutableArray {
        let languageList = NSMutableArray.init()
        
        let languageOne = Language.init(language: "English", code: "en")
        let languageTwo = Language.init(language: "Swedish", code: "sv-SE")
        
        languageList.add(languageOne)
        languageList.add(languageTwo)
        
        return languageList
    }
    
    class func clearQuestionOrAnswerData() {
        DiaryHandler.shared.questionType = ""
        DiaryHandler.shared.question = nil
        DiaryHandler.shared.questionOption = nil
        DiaryHandler.shared.questionOptionList = NSMutableArray.init()
        DiaryHandler.shared.rating = 0.0
        
        DiaryHandler.shared.answeredQuestionType = ""
        DiaryHandler.shared.answeredQuestion = nil
//        DiaryHandler.shared.answeredQuestionOption = nil
        DiaryHandler.shared.answeredQuestionOptionList = NSMutableArray.init()
        DiaryHandler.shared.answeredQuestionRating = 0.0
    }
    
    class func clearDataBase() {
        var dataBase: [SRKObject.Type] = []
        dataBase = [DBUser.self, DBContact.self, DBAuthority.self, DBAnsweredQuestion.self, DBQuestion.self, DBQuestionnaire.self, DBQuestionType.self, DBRole.self, DBQuestionOption.self, DBInfo.self, DBNote.self, DBNotification.self, DBMyContact.self, DBAboutUs.self, DBSession.self]
        dataBase.forEach { (data) in
            data.query().fetch().remove()
        }
    }
    
    class func getGroupChatId(groupChatId:String) -> String {
        return groupChatId
    }
    
    class func postAnalytics(analyticsType:String) {
        APIAnalyticsHandler.shared.postAppAnalytics(analyticsType: analyticsType, success: { (message) in
            print(message)
        }, failure: { (message) in
            print(message)
        })
    }
    
    class func getYoutubeId(youtubeUrl: String) -> String? {
        return URLComponents(string: youtubeUrl)?.queryItems?.first(where: { $0.name == "v" })?.value
    }
    
    class func slideImageList() -> [String] {
        return ["slide_img_one", "slide_img_two", "slide_img_three", "slide_img_four", "slide_img_five", "slide_img_six"]
    }
    
    class func includeCountryCodeIfItIsMobileNumber(inputStr: String) -> String {
        print("Input: \(inputStr)")
        let trimmedString = String(inputStr.filter { !" ".contains($0) })

        return finalSwedenPhoneNumber(phoneNumber: trimmedString)
    }
    
    class func finalSwedenPhoneNumber(phoneNumber:String) -> String {
        /**
         Input: +460737274140           Output: +46737274140
         Input: +46737274140             Output: +46737274140
         Input: 0737274140                 Output: +46737274140
         Input: 737274140                   Output: +46737274140
         Input: +9779849055193         Output: +9779849055193
         */
        var finalPhoneNumber = phoneNumber
        if finalPhoneNumber.hasPrefix("+460") { //Remove 0
            finalPhoneNumber.remove(at: "+46".endIndex)
        } else if finalPhoneNumber.hasPrefix("+46"){ //Do nothing
            
        } else if finalPhoneNumber.hasPrefix("46") {
            if finalPhoneNumber.hasPrefix("460") { // Remove 0
                finalPhoneNumber.remove(at: "46".endIndex)
            }
            finalPhoneNumber = "+\(finalPhoneNumber)"
        } else if finalPhoneNumber.hasPrefix("0"){
            finalPhoneNumber.remove(at: finalPhoneNumber.startIndex)
            finalPhoneNumber = "+46\(finalPhoneNumber)"

        } else if !finalPhoneNumber.hasPrefix("+"){ // No country code then add default +46
            finalPhoneNumber = "+46\(finalPhoneNumber)"

        } else { //Other than sweden with relevant country code
            
        }
        print ("Output: \(finalPhoneNumber)")
        return finalPhoneNumber
    }
    
    class func sideMenuList() -> NSMutableArray {
        let sideMenuList = NSMutableArray.init()
        
        let one = SideMenu.init(image: "ic_survey", title: NSLocalizedString("Survey", comment: ""), viewController: Constants.StoryboardId.SURVEY_VC)
        let two = SideMenu.init(image: "notification", title: NSLocalizedString("Notification", comment: ""), viewController: Constants.StoryboardId.NOTIFICATION_VC)
        let three = SideMenu.init(image: "note", title: NSLocalizedString("Note", comment: ""), viewController: Constants.StoryboardId.NOTE_VC)
        let four = SideMenu.init(image: "info", title: NSLocalizedString("Link", comment: ""), viewController: Constants.StoryboardId.LINKS_VC)
        let five = SideMenu.init(image: "ic_answer", title: NSLocalizedString("Answer", comment: ""), viewController: Constants.StoryboardId.ANSWERED_QUESTION_VC)
        let six = SideMenu.init(image: "feedback", title: NSLocalizedString("Feedback", comment: ""), viewController: Constants.StoryboardId.FEEDBACK_VC)
        let seven = SideMenu.init(image: "contact", title: NSLocalizedString("Contact Us", comment: ""), viewController: Constants.StoryboardId.CONTACT_US_VC)
        let eight = SideMenu.init(image: "privacyPolicy", title: NSLocalizedString("Privacy Policy", comment: ""), viewController: Constants.StoryboardId.WEB_VIEW_VC)
        let nine = SideMenu.init(image: "about", title: NSLocalizedString("About", comment: ""), viewController: Constants.StoryboardId.WEB_VIEW_VC)
        let ten = SideMenu.init(image: "password", title: NSLocalizedString("Change Password", comment: ""), viewController: Constants.StoryboardId.CHANGE_PASSWORD_VC)
        let eleven = SideMenu.init(image: "logout_white", title: NSLocalizedString("Log Out", comment: ""), viewController: Constants.StoryboardId.LOGIN_VC)
        
        sideMenuList.add(one)
        sideMenuList.add(two)
        sideMenuList.add(three)
        sideMenuList.add(four)
        sideMenuList.add(five)
        sideMenuList.add(six)
        sideMenuList.add(seven)
        sideMenuList.add(eight)
        sideMenuList.add(nine)
        sideMenuList.add(ten)
        sideMenuList.add(eleven)
        
        return sideMenuList
    }
}
