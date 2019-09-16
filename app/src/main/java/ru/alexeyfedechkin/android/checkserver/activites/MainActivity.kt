package ru.alexeyfedechkin.android.checkserver.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.R
import ru.alexeyfedechkin.android.checkserver.SavingKey
import ru.alexeyfedechkin.android.checkserver.ServerAdapter

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
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.context_menu, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val server = serverAdapter.servers[info.position]
        when (item.itemId){
            R.id.contextMenu_delete -> {
                db.deleteServer(server)
                serverAdapter.servers = db.getServers()
                serverAdapter.notifyDataSetChanged()
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

    override fun onResume() {
        super.onResume()
        serverAdapter.servers = db.getServers()
        serverAdapter.notifyDataSetChanged()
    }

    /**
     *
     */
    fun btnDeleteClick(view: View) {
    }

    /**
     * open addServerActivity
     */
    fun btnAddClick(view: View) {
        intent = Intent(this, AddServerActivity::class.java)
        startActivity(intent)
    }
}
