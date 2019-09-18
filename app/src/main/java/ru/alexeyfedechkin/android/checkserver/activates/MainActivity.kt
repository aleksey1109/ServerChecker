package ru.alexeyfedechkin.android.checkserver.activates

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.R
import ru.alexeyfedechkin.android.checkserver.SavingKey
import ru.alexeyfedechkin.android.checkserver.ServerAdapter
import ru.alexeyfedechkin.android.checkserver.enums.ServerStatus
import ru.alexeyfedechkin.android.checkserver.network.Net
import java.util.*


/**
 * main activity
 */
class MainActivity : AppCompatActivity() {

    private val db:DB = DB()
    private lateinit var  serverAdapter:ServerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db.init(applicationContext)
        serverAdapter = ServerAdapter(this, db.getServers())
        val serverList = findViewById<ListView>(R.id.serverList)
        serverList.adapter = serverAdapter
        registerForContextMenu(serverList)
        setProgress()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                updateServerStatus()
            }
        }, 0, 2000)
    }

    /**
     * update list of server when object was added to database
     */
    private fun updateListServer(){
        serverAdapter.servers = db.getServers()
        serverAdapter.notifyDataSetChanged()
    }

    /**
     * show progress bar for item
     */
    private fun setProgress(){
        for (srv in serverAdapter.servers){
            val item = serverList.getChildAt(serverAdapter.servers.indexOf(srv))
            if (item != null){
                item.findViewById<ProgressBar>(R.id.progressBar_loading).visibility = ProgressBar.VISIBLE
                val status = item.findViewById<RelativeLayout>(R.id.status)
                val textStatus = item.findViewById<TextView>(R.id.textView_status)
                status.visibility = RelativeLayout.INVISIBLE
                status.visibility = RelativeLayout.GONE
                textStatus.visibility = TextView.INVISIBLE
                textStatus.visibility = TextView.GONE
            }
        }
    }

    /**
     * check server availability
     */
    private fun updateServerStatus(){
        for (srv in serverAdapter.servers){
            doAsync {
                val hostStatus = Net.checkServerStatus(srv, applicationContext)
                val rc = Net.checkServerResponse(srv)
                val item = serverList.getChildAt(serverAdapter.servers.indexOf(srv))
                val status = item.findViewById<RelativeLayout>(R.id.status)
                val textStatus = item.findViewById<TextView>(R.id.textView_status)
                val loading = item.findViewById<ProgressBar>(R.id.progressBar_loading)
                uiThread {
                    if (textStatus.currentTextColor == Color.RED){
                        textStatus.setTextColor(Color.BLACK)
                    }
                    when(hostStatus) {
                        ServerStatus.OFFLINE -> {
                            status!!.background =
                                resources!!.getDrawable(R.drawable.circle_offline)
                            textStatus?.text = resources!!.getString(R.string.offline)
                        }
                        ServerStatus.ONLINE -> {
                            status!!.background =
                                resources!!.getDrawable(R.drawable.circle_online)
                            textStatus?.text = resources!!.getString(R.string.online)
                        }
                        ServerStatus.INVALID_RESPONSE_CODE -> {
                            status!!.background =
                                resources!!.getDrawable(R.drawable.circle_invalid_resonse_code)
                            textStatus?.text = rc.toString()
                            textStatus?.setTextColor(resources.getColor(R.color.red))
                        }
                        ServerStatus.NO_INTERNET_ACCESS ->{
                            textStatus?.text = resources.getString(R.string.no_internet_access)
                            status!!.background = resources.getDrawable(R.drawable.circle_no_internet_access)
                        }
                    }
                    loading.visibility = ProgressBar.INVISIBLE
                    loading.visibility = ProgressBar.GONE
                    status.visibility = RelativeLayout.VISIBLE
                    textStatus.visibility = TextView.VISIBLE
                }
            }
        }
    }

    /**
     * set context menu layout
     * @param menu
     * @param v
     * @param menuInfo
     */
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?,
                                     menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.context_menu, menu)

    }

    /**
     * handle on context menu event
     * @param item instance of selected item
     * @return true if menu is handled
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val server = serverAdapter.servers[info.position]
        when (item.itemId){
            R.id.contextMenu_delete -> {
                db.deleteServer(server)
                updateListServer()
                return true
            }
            R.id.contextMenu_edit -> {
                val intent = Intent(this, EditServerActivity::class.java)
                intent.putExtra(SavingKey.SERVER_NAME_TO_EDIT_KEY, server.name)
                startActivity(intent)
                return true
            }
        }
        return false
    }

    /**
     * update server list and status on activity shown again
     */
    override fun onResume() {
        super.onResume()
        updateListServer()
        setProgress()
        updateServerStatus()
    }

    /**
     * update server status
     */
    fun btnUpdateClick(view: View) {
        setProgress()
        updateServerStatus()
    }

    /**
     * open addServerActivity
     */
    fun btnAddClick(view: View) {
        intent = Intent(this, AddServerActivity::class.java)
        startActivity(intent)
    }

}
