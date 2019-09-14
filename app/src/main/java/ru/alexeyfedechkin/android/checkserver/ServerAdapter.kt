package ru.alexeyfedechkin.android.checkserver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.Network.Net

/**
 * custom listView adapter to show server status
 * @property servers list of server
 * @constructor
 * @param context activity context
 */
class ServerAdapter(
    context: Context,
    // list of servers
    private var servers: ArrayList<Server>) : BaseAdapter() {
    private var layoutInflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    /**
     * get view with server item
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null){
            view = layoutInflater.inflate(R.layout.server_item, parent,false)
        }
        val server = getServer(position)
        view?.findViewById<TextView>(R.id.textView_serverName)?.text = server.name
        view?.findViewById<TextView>(R.id.editText_hostname)?.text   = server.hostname
        val status = view?.findViewById<RelativeLayout>(R.id.status)
        when(Net.checkStatus(server)){
            ServerStatus.OFFLINE -> status!!.background = view?.resources!!.getDrawable(R.drawable.circle_offline)
            ServerStatus.ONLINE -> status!!.background = view?.resources!!.getDrawable(R.drawable.circle_online)
        }
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