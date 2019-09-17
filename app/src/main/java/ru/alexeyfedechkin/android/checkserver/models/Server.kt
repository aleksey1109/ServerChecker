package ru.alexeyfedechkin.android.checkserver.models

import android.annotation.SuppressLint
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
     * server hostname
     */
    var hostname:String = ""
    /**
     * connecting port
     */
    var port:Int = 0
    /**
     * wrapper for ease of use of enumeration instead of string
     */
    var protocol: Protocol
        /**
         * parse string to enum
         */
        get() {
            return Protocol.valueOf(protocolDescription)
        }
        @SuppressLint("DefaultLocale")
        /**
         * convert Enum to String
         */
        set(value) {
            protocolDescription = value.protocol.toUpperCase()
        }
    /**
     * using protocol to connecting
     * @note saving in realm db as String because realm don't support persist enum
     */
    private var protocolDescription:String = Protocol.HTTP.protocol
}