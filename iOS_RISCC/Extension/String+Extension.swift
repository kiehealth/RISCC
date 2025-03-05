//
//  String+Extension.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import Foundation

extension String {
    
    func localized() ->String {
//        print("Device Language Code: \(Locale.current.languageCode)")
        if let _ = UserDefaults.standard.string(forKey: "language") {} else {
            // we set a default, just in case
            UserDefaults.standard.set("en", forKey: "language")
            UserDefaults.standard.synchronize()
        }
        
        let language = UserDefaults.standard.string(forKey: "language")
        let path = Bundle.main.path(forResource: language, ofType: "lproj")
        let bundle = Bundle(path: path!)
        
        return NSLocalizedString(self, tableName: nil, bundle: bundle!, value: "", comment: "")
    }
    
    func base64Encoded() -> String {
        let plainData = data(using: String.Encoding.utf8)
        let base64String = plainData?.base64EncodedString()
        return base64String!
    }
    
    func base64Decoded() -> String {
        let decodedData = NSData(base64Encoded: self, options: [])
        let decodedString = NSString(data: decodedData! as Data, encoding: String.Encoding.utf8.rawValue)
        return decodedString as! String
    }
    
    var words: [String] {
        var words: [String] = []
        enumerateSubstrings(in: startIndex..<endIndex, options: .byWords) { word,_,_,_ in
            guard let word = word else { return }
            words.append(word)
        }
        return words
    }
    
    var isNumeric : Bool {
        return NumberFormatter().number(from: self) != nil
    }
    
    var htmlToAttributedString: NSAttributedString? {
        guard let data = data(using: .utf8) else { return NSAttributedString() }
        do {
            return try NSAttributedString(data: data, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding:String.Encoding.utf8.rawValue], documentAttributes: nil)
        } catch {
            return NSAttributedString()
        }
    }
    var htmlToString: String {
        return htmlToAttributedString?.string ?? ""
    }
    
    
    //    - (NSString *)hmac
    //    {
    //    NSString *key = @"MWMzOTljMzI4Y2EyN2M5OTBiZTMyMTUwZGRjNDc";
    //    NSData *keyData = [key dataUsingEncoding:NSUTF8StringEncoding];
    //    NSData *paramData = [self dataUsingEncoding:NSUTF8StringEncoding];
    //    NSMutableData* hash1 = [NSMutableData dataWithLength:CC_SHA256_DIGEST_LENGTH ];
    //    CCHmac(kCCHmacAlgSHA256, keyData.bytes, keyData.length, paramData.bytes, paramData.length, hash1.mutableBytes);
    //    return [hash1 hexadecimalString];
    //    }
}
