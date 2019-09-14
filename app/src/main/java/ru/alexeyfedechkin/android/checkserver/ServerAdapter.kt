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
 * TODO
 *
 * @property servers
 * @constructor
 * TODO
 *
 * @param context
 */
class ServerAdapter(
    context: Context,
    var servers: ArrayList<Server>) : BaseAdapter() {
    var layoutInflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    /**
     * TODO
     *
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
        val server = getProduct(position)
        view?.findViewById<TextView>(R.id.textView_serverName)?.text = server.name
        val status = view?.findViewById<RelativeLayout>(R.id.status)
        when(Net.checkStatus(server)){
            ServerStatus.OFFLINE -> status!!.background = view?.resources!!.getDrawable(R.drawable.circle_offline)
            ServerStatus.ONLINE -> status!!.background = view?.resources!!.getDrawable(R.drawable.circle_online)
        }
        return view!!
    }

    /**
     * TODO
     *
     * @param position
     * @return
     */
    fun getProduct(position: Int):Server{
        return getItem(position) as Server
    }

    /**
     * TODO
     *
     * @param position
     * @return
     */
    override fun getItem(position: Int): Any {
        return servers[position]
    }

    /**
     * TODO
     *
     * @param position
     * @return
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * TODO
     *
     * @return
     */
    override fun getCount(): Int {
        return servers.count()
    }
}