//
//  Double+Extension.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import Foundation

extension Double {
    func roundTo(places:Int) -> Double {
        let divisor = pow(10.0, Double(places))
        return (self * divisor).rounded() / divisor
    }
}
