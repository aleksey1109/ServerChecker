package ru.alexeyfedechkin.android.checkserver

import android.content.Context
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import ru.alexeyfedechkin.android.checkserver.models.Server

/**
 * realm framework wrapper to perform operation with database
 * @property context activity context
 */
class DB {

    private lateinit var context: Context

    fun init(context: Context){
        this.context = context
    }

    /**
     * get all server store in database
     * @see Server
     * @return List of servers
     */
    fun getServers():ArrayList<Server>{
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Server::class.java).findAll()
        val results = realm.copyFromRealm(result)
        realm.close()
        return results as ArrayList<Server>
    }

    /**
     * save server instance in database
     * operation is blocking
     * @param server server to save in database
     */
    fun saveServer(server: Server) {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            realm.insert(server)
            realm.commitTransaction()
            realm.close()
        } catch (ex: RealmPrimaryKeyConstraintException){
            //if insert returned exception
            realm.close()
            throw ex
        }

    }
}