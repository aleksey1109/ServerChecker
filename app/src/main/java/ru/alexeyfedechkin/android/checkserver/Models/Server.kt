package ru.alexeyfedechkin.android.checkserver.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * POJO to realmObject
 * save server data in DB
 */
open class Server : RealmObject(){
    /**
     *name of server
     */
    @PrimaryKey var name:String = ""
    /**
     * expected server response code
     */
    var responseCode:Int = 0
    /**
     * server url
     */
    var hostname:String = ""
}
