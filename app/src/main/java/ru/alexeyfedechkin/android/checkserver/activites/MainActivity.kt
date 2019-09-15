package ru.alexeyfedechkin.android.checkserver.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.R
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
        findViewById<ListView>(R.id.serverList).adapter = serverAdapter
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
