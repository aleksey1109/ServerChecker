package ru.alexeyfedechkin.android.checkserver.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Server(): RealmObject(){
    @PrimaryKey var id:Long = 0
    /**
     *
     */
    var name:String = ""
    /**
     *
     */
    var responseCode:Int = 0
    /**
     *
     */
    var hostname:String = ""
}
