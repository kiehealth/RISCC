//
//  ConnectionManager.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//


import UIKit
import Reachability

class ConnectionManager {

    static let sharedInstance = ConnectionManager()
    private var reachability : Reachability?
    
    func observeReachability(){


        do {
            self.reachability = try Reachability()
            NotificationCenter.default.addObserver(self, selector:#selector(self.reachabilityChanged), name: NSNotification.Name.reachabilityChanged, object: nil)
            try self.reachability?.startNotifier()
        }
        catch(let error) {
            print("Error occured while starting reachability notifications : \(error.localizedDescription)")
        }
    }
    
    @objc func reachabilityChanged(note: Notification) {
        let reachability = note.object as? Reachability
        switch reachability?.connection {
        case .cellular:
            print("Network available via Cellular Data.")
            break
        case .wifi:
            print("Network available via WiFi.")
            break
        case .none:
            print("Network is not available.")
            break
        case .some(.none):
            print("Network is none.")
        case .some(.unavailable):
            print("Network is not unavailable.")

        }
    }
}
