//
//  AppDelegate.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import IQKeyboardManagerSwift
import SharkORM
import CoreLocation
import DropDown
import UserNotifications
import Toast_Swift
import AVFoundation
import Reachability
import SwiftyJSON

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, SRKDelegate, UNUserNotificationCenterDelegate {

    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        if let userInfo = launchOptions?[UIApplication.LaunchOptionsKey.remoteNotification] { // Launched from PushNotification
            let notificationInfo = JSON(userInfo)
        } else { //Not Launched from PushNotification

        }
        
        
        do {
            if #available(iOS 10.0, *) {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playback, mode: AVAudioSession.Mode.default, options: [.mixWithOthers, .allowAirPlay, .defaultToSpeaker])
                try AVAudioSession.sharedInstance().setActive(true)
            } else {
                // Fallback on earlier versions
            }
        } catch {
            print(error)
        }
        
        if #available(iOS 10, *) {
            UNUserNotificationCenter.current().requestAuthorization(options:[.badge, .alert, .sound]){ (granted, error) in }
            application.registerForRemoteNotifications()
        } else if #available(iOS 9, *) {
            UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [.badge, .sound, .alert], categories: nil))
            UIApplication.shared.registerForRemoteNotifications()
        } else if #available(iOS 8, *) {
            UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [.badge, .sound, .alert], categories: nil))
            UIApplication.shared.registerForRemoteNotifications()
        } else { // iOS 7 support
            UIApplication.shared.registerForRemoteNotifications(matching: [.badge, .sound, .alert])
        }
        
        ConnectionManager.sharedInstance.observeReachability()
        
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true
        
        SharkORM.setDelegate(self)
        SharkORM.openDatabaseNamed(Constants.Database.DBMODEL_NAME)
        SharkORM.setVersion(Constants.Database.DBMODEL_VERSION)
        DropDown.startListeningToKeyboard()
        
        
//        UNUserNotificationCenter.current().delegate = self
//        FirebaseApp.configure()
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
//        self.timer.invalidate()
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.UserOut.rawValue)
        NotificationCenter.default.post(name: NSNotification.Name(SIGNALKEY_APP_DID_ENTER_BACKGROUND), object: nil)

    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
        Util.postAnalytics(analyticsType: ANALYTICS_TYPE.UserIn.rawValue)
        
        NotificationCenter.default.post(name: NSNotification.Name(SIGNALKEY_APP_WILL_ENTER_FOREGROUND), object: nil)

    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        application.applicationIconBadgeNumber = 0
        let session = DBSession.init()
        session.commit()
        
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
//        connect()
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let deviceTokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
        print("APNs device token: \(deviceTokenString)")
        DiaryHandler.shared.deviceToken = deviceTokenString
        /*
         If user is already loggedin, update device token (User Profile Update API)
         */
        if DBUserHandler.shared.getLoggedUser() != nil {
            let postParameter = NSMutableDictionary.init()
            postParameter.setValue(DEVICE_IOS, forKey: "devicePlatform")
            postParameter.setValue(DiaryHandler.shared.deviceToken, forKey: "deviceToken")
            postParameter.setValue(PUSH_NOTIFICATION_MODE, forKey: "iosNotificationMode")

            APIUserHandler.shared.updateUser(postParameter: postParameter, success: { (message) in
                print(message)
            }) { (message) in
                print(message)
            }
        }
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("APNs registration failed: \(error)")
        DiaryHandler.shared.deviceToken = "00000000000000"
    }
    
//    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
//        let notificationInfo = JSON(userInfo)
//        SurveyHandler.shared.moveToNotificaitonVC = true
//        self.moveToHome()
//    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        let notificationInfo = JSON(userInfo)
        print("NotificationInfo: \(notificationInfo)")

        if(application.applicationState == UIApplication.State.active) {
            //app is currently active, can update badges count here

        }else if(application.applicationState == UIApplication.State.background){
            //app is in background, if content-available key of your notification is set to 1, poll to your backend to retrieve data and update your interface here

        }else if(application.applicationState == UIApplication.State.inactive){
            //app is transitioning from background to foreground (user taps notification), do what you need when user taps here
            print("transitioning from background to foreground")
            DiaryHandler.shared.moveToNotificaitonVC = true
            DiaryHandler.shared.notificaitonId = notificationInfo["id"].stringValue
            self.moveToHome()
        }
    }
    
    func moveToHome() {
//        let mainTabVC = UIStoryboard(name: STORYBOARD_HOME, bundle: nil).instantiateViewController(withIdentifier: IDENTIFIER_MAIN_TAB_VC) as! MainTabVC
//        window?.rootViewController = mainTabVC
    }
    
    
}

