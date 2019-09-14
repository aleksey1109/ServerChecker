package ru.alexeyfedechkin.android.checkserver

import android.content.Context
import io.realm.Realm
import io.realm.RealmResults
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import ru.alexeyfedechkin.android.checkserver.Models.Server

class DB {

    lateinit var context: Context

    fun init(context:Context){
        this.context = context
    }

    /**
     * TODO
     *
     * @return
     */
    fun getServers():ArrayList<Server>{
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Server::class.java).findAll()
        val results = realm.copyFromRealm(result)
        realm.close()
        return results as ArrayList<Server>
    }


    fun saveServer(server: Server) {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            realm.insert(server)
            realm.commitTransaction()
            realm.close()
        } catch (ex: RealmPrimaryKeyConstraintException){
            realm.close()
            throw ex
        }

    }
}