package ru.alexeyfedechkin.android.checkserver

import android.content.Context
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import ru.alexeyfedechkin.android.checkserver.models.Server
import java.lang.Exception

/**
 * realm framework wrapper to perform operation with database
 * @property context activity context
 */
class DB {

    private lateinit var context: Context

    /**
     * set context
     * @param context activity context
     */
    fun init(context: Context){
        this.context = context
    }

    /**
     * init new realm session
     * @return instance of realm
     */
    private fun getSession():Realm{
        Realm.init(context)
        return Realm.getDefaultInstance()
    }

    /**
     * get all server store in database
     * @see Server
     * @return List of servers
     */
    fun getServers():ArrayList<Server>{
        val realm = getSession()
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
        val realm = getSession()
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

    /**
     * update server
     * @param server current instance of server
     * @param sourceServer instance of server that needed to update
     */
    fun updateServer(server: Server, sourceServer:Server){
        val realm = getSession()
        realm.executeTransactionAsync(){realm ->
            realm.insertOrUpdate(server)
            if (server.name != sourceServer.name){
                val server = realm.where(Server::class.java).equalTo("name", sourceServer.name).findAll()
                server.deleteAllFromRealm()
            }
        }
        realm.close()
    }

    /**
     * delete server
     * @param server instanse of server to delete
     */
    fun deleteServer(server: Server){
        val realm = getSession()
        try {
            realm.beginTransaction()
            val server = realm.where(Server::class.java).equalTo("name", server.name).findAll()
            server.deleteAllFromRealm()
            realm.commitTransaction()
            realm.close()
        } catch (ex:Exception){
            realm.close()
        }
    }

    /**
     * get server by name from database
     * blocking
     * @param name name of server
     * @return server instanse from db by giving name
     */
    fun getServerByName(name:String): Server {
        val realm = getSession()
        val realmServer = realm.where(Server::class.java).equalTo("name", name).findAll()
        val server = realm.copyFromRealm(realmServer)
        realm.close()
        return server[0]!!
    }
}