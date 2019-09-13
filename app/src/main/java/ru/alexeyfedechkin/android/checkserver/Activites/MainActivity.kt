package ru.alexeyfedechkin.android.checkserver.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.R
import ru.alexeyfedechkin.android.checkserver.ServerAdapter

class MainActivity : AppCompatActivity() {

    private val servers:ArrayList<Server> = {
        DB.getServers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val serverAdapter = ServerAdapter(this, servers)
        findViewById<ListView>(R.id.serverList).adapter = serverAdapter
    }

    /**
     * TODO
     *
     * @param view
     */
    fun btnDeleteClick(view: View) {}

    /**
     * TODO
     *
     * @param view
     */
    fun btnAddClick(view: View) {}
}
