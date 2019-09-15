package ru.alexeyfedechkin.android.checkserver.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import ru.alexeyfedechkin.android.checkserver.enums.Protocol

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
    /**
     *
     */
    var port:Int = 0
    /**
     *
     */
     var protocol: Protocol
        get() {
            return Protocol.valueOf(protocolDescription)
        }
        set(value) {
            protocolDescription = value.protocol.toUpperCase()
        }
    private var protocolDescription:String = Protocol.HTTP.protocol
}