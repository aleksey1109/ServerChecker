package ru.alexeyfedechkin.android.checkserver.Models

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import ru.alexeyfedechkin.android.checkserver.Net
import ru.alexeyfedechkin.android.checkserver.ServerStatus

open class Server(
    @PrimaryKey var id:Long,
    /**
     *
     */
    var name:String,
    /**
     *
     */
    var responseCode:Int
): RealmObject(){
    val status:ServerStatus by lazy {
        Net.checkServerStatus(responseCode)
    }
}
