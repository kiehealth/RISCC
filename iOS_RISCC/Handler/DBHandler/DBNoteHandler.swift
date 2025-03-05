//
//  DBNoteHandler.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import UIKit
import SharkORM

private let instance = DBNoteHandler()

class DBNoteHandler: NSObject {
    
    class var shared: DBNoteHandler {
        return instance
    }
    
    func getNotes() -> NSMutableArray {
        let results: SRKResultSet = DBNote.query().fetch()
        let noteList = NSMutableArray.init()
        for case let note as DBNote in results {
            noteList.add(note)
        }
        return noteList
    }
    
    func getAlreadySavedData(noteId:String, success:@escaping((_ dbNote:DBNote)->Void), failure:@escaping((_ message:String)->Void)) {
        let results: SRKResultSet = DBNote.query().where(withFormat: "isSync = %@ AND noteId = %@", withParameters: [true, noteId]).fetch()
        if results.count > 0 {
            let result = results[0]
            let note = result as! DBNote
            success(note)
        } else {
            failure("failure")
        }
    }
    
    func getNotesThatAreNotSync() -> NSMutableArray {
        let results: SRKResultSet = DBNote.query().where(withFormat: "isSync = %@", withParameters: [false]).fetch()
        let noteList = NSMutableArray.init()
        for case let note as DBNote in results {
            noteList.add(note)
        }
        return noteList
    }
    
    func getNotesThatAreUnsyncedUpdate() -> NSMutableArray {
        let results: SRKResultSet = DBNote.query().where(withFormat: "unsyncedUpdate = %@", withParameters: [true]).fetch()
        let noteList = NSMutableArray.init()
        for case let note as DBNote in results {
            noteList.add(note)
        }
        return noteList
    }
    
    func deleteNote(noteId:String) {
        DBNote.query().where(withFormat: "noteId = %@", withParameters: [noteId]).fetch().remove()
    }
    
    func deleteAllNoteWithSyncValue(isSync:Bool) {
        DBNote.query().where(withFormat: "isSync = %@", withParameters: [isSync]).fetch().remove()
    }
    
    func deleteAllNotes() {
        DBNote.query().fetch().remove()
    }

}
