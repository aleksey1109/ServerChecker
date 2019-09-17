package ru.alexeyfedechkin.android.checkserver

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ru.alexeyfedechkin.android.checkserver.models.Server

/**
 * custom listView adapter to show server status
 * @property servers list of server
 * @constructor set application context and list of server
 * @param context activity context
 */
class ServerAdapter(
    context: Context,
    // list of servers
    var servers: ArrayList<Server>) : BaseAdapter() {
    private var layoutInflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    /**
     * get view with server item by giving position
     * @param position position of item
     * @param convertView
     * @param parent
     * @return view with server item
     */
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null){
            view = layoutInflater.inflate(R.layout.server_item, parent,false)
        }
        val server = getServer(position)
        view?.findViewById<TextView>(R.id.textView_serverName)?.text = server.name
        view?.findViewById<TextView>(R.id.textView_hostname)?.text=
            "${server.protocol.protocol}://${server.hostname}:${server.port}"
        return view!!
    }

    /**
     * get Server by giving position
     * @param position server index
     * @return server by giving position
     */
    private fun getServer(position: Int):Server{
        return getItem(position) as Server
    }

    /**
     * get server by position
     * @param position item position
     * @return server item
     */
    override fun getItem(position: Int): Any {
        return servers[position]
    }

    /**
     * get server id
     * for this adapter item id and position is equals
     * @param position item position
     * @return requested id of item
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * get count of servers in list
     * @see ServerAdapter.servers
     * @return count of object in listView
     */
    override fun getCount(): Int {
        return servers.count()
    }
}