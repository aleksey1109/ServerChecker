package ru.alexeyfedechkin.android.checkserver

import android.content.Context
import io.realm.Realm
import io.realm.RealmResults
import ru.alexeyfedechkin.android.checkserver.Models.Server

class DB {

    private lateinit var realm:Realm

    fun init(context:Context){
        Realm.init(context)
        realm = Realm.getDefaultInstance()
    }

    /**
     * TODO
     *
     * @return
     */
    fun getServers():ArrayList<Server>{
        val result = realm.where(Server::class.java).findAll()
        return RealmResultsToArayList(result)
    }

    /**
     * TODO
     *
     * @param results
     * @return
     */
    private fun RealmResultsToArayList(results: RealmResults<Server>): ArrayList<Server> {
        val res:ArrayList<Server> = ArrayList()
        for (server in results.toArray()){
            res.add(server as Server)
        }
        return res
    }
}