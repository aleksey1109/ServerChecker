package ru.alexeyfedechkin.android.checkserver.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import ru.alexeyfedechkin.android.checkserver.R

class AddServerActivity : AppCompatActivity() {
    private val serverName:EditText by lazy  {
        findViewById<EditText>(R.id.editText_serverName)
    }
    private val hostname:EditText by lazy {
        findViewById<EditText>(R.id.editText_hostname)
    }
    private val responseCode:EditText by lazy{
        findViewById<EditText>(R.id.editText_responseCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)
    }

    /**
     * TODO
     *
     */
    override fun onBackPressed() {
        super.onBackPressed()

    }
}
