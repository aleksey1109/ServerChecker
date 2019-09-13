package ru.alexeyfedechkin.android.checkserver.Activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import io.realm.Realm
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.R
import ru.alexeyfedechkin.android.checkserver.ServerAdapter

class MainActivity : AppCompatActivity() {

    private val db:DB = DB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db.init(applicationContext)
        val serverAdapter = ServerAdapter(this, db.getServers())
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
    fun btnAddClick(view: View) {
        intent = Intent(this, AddServerActivity::class.java)
        startActivity(intent)
    }
}
