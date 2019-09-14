package ru.alexeyfedechkin.android.checkserver.Activites

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.Network.Http
import ru.alexeyfedechkin.android.checkserver.R

/**
 * activity to add new servers
 */
class AddServerActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "AddServerActivity"
        private const val SERVER_NAME_KEY = "server_key"
        private const val HOSTNAME_KEY= "hostname_key"
        private const val RESPONSE_CODE_KEY= "response_code"
    }

    private val serverName:EditText by lazy  {
        findViewById<EditText>(R.id.editText_serverName)
    }

    private val hostname:EditText by lazy {
        findViewById<EditText>(R.id.editText_hostname)
    }

    private val responseCode:EditText by lazy{
        findViewById<EditText>(R.id.editText_responseCode)
    }

    private val db:DB = DB(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)
        serverName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    serverName.setTextColor(resources.getColor(R.color.black))
                }
            }
        })

        hostname.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    hostname.setTextColor(resources.getColor(R.color.black))
                }
            }
        })

        responseCode.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    responseCode.setTextColor(resources.getColor(R.color.black))
                }
            }
        })
    }

    /**
     * store fields text
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SERVER_NAME_KEY, serverName.text.toString())
        outState.putString(HOSTNAME_KEY, hostname.text.toString())
        outState.putString(RESPONSE_CODE_KEY, responseCode.text.toString())
    }

    /**
     * restore fields text
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serverName.setText(savedInstanceState.getString(SERVER_NAME_KEY))
        hostname.setText(savedInstanceState.getString(HOSTNAME_KEY))
        responseCode.setText(savedInstanceState.getString(RESPONSE_CODE_KEY))
    }


    /**
     *
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * validate data and
     * save data in database
     */
    fun btnSaveClick() {
        val server = Server()
        if (serverName.text.isNullOrEmpty()){
            serverName.setTextColor(resources.getColor(R.color.red))
        } else if (hostname.text.isNullOrEmpty()){
            hostname.setTextColor(resources.getColor(R.color.red))
        } else if (responseCode.text.isNullOrEmpty() && Http.validateResponseCode(responseCode.text.toString().toInt())){
            responseCode.setTextColor(resources.getColor(R.color.red))
        }
        server.name = serverName.text.toString()
        server.hostname = hostname.text.toString()
        server.responseCode = responseCode.text.toString().toInt()
        try {
            db.saveServer(server)
            finish()
        } catch (ex: RealmPrimaryKeyConstraintException){
            Toast.makeText(applicationContext, resources.getText(R.string.nameAlreadyExist), Toast.LENGTH_SHORT)
            serverName.setTextColor(resources.getColor(R.color.red))
        }
    }
    /**
     * clear all fields
     */
    fun btnCLearClick() {
        serverName.text.clear()
        hostname.text.clear()
        responseCode.text.clear()
    }
    /**
     * back to the main activity
     */
    fun btnBackClick() {
        onBackPressed()
    }
}
