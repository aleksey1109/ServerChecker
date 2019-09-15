package ru.alexeyfedechkin.android.checkserver.activites

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.enums.Protocol
import ru.alexeyfedechkin.android.checkserver.models.Server
import ru.alexeyfedechkin.android.checkserver.network.Http
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
        private const val PORT_KEY = "port_key"
        private const val PROTOCOL_KEY = "protocol_key"
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
    private val port:EditText by lazy {
        findViewById<EditText>(R.id.editText_port)
    }
    private val protocol:Spinner by lazy {
        findViewById<Spinner>(R.id.spinner_protocol)
    }

    private val db:DB = DB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)
        db.init(applicationContext)
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
        port.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    port.setTextColor(resources.getColor(R.color.black))
                }
            }
        })

        val arrayAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item)
        for (protocol in Protocol.values()){
            arrayAdapter.add(protocol.protocol)
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val protocolSpinner = findViewById<Spinner>(R.id.spinner_protocol)
        protocolSpinner.adapter = arrayAdapter
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
        outState.putString(PORT_KEY, port.text.toString())
        outState.putInt(PROTOCOL_KEY, protocol.selectedItemPosition)
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
        port.setText(savedInstanceState.getString(PORT_KEY))
        protocol.setSelection(savedInstanceState.getInt(PROTOCOL_KEY))
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
    fun btnSaveClick(view: View) {
        val server = Server()
        if (!validateData()){
            return
        }
        server.name = serverName.text.toString()
        server.hostname = hostname.text.toString()
        server.responseCode = responseCode.text.toString().toInt()
        server.port = port.text.toString().toInt()
        server.protocol = Protocol.values()[protocol.selectedItemPosition]
        try {
            db.saveServer(server)
            finish()
        } catch (ex: RealmPrimaryKeyConstraintException){
            Toast.makeText(applicationContext, resources.getText(R.string.nameAlreadyExist), Toast.LENGTH_SHORT)
            serverName.setTextColor(resources.getColor(R.color.red))
        }
    }

    private fun validateData():Boolean{
        if (serverName.text.isNullOrEmpty()){
            serverName.setTextColor(resources.getColor(R.color.red))
        }
        if (hostname.text.isNullOrEmpty()){
            hostname.setTextColor(resources.getColor(R.color.red))
        }
        if (responseCode.text.isNullOrEmpty()){
            responseCode.setTextColor(resources.getColor(R.color.red))
        } else if (!Http.validateResponseCode(responseCode.text.toString().toInt())){
            responseCode.setTextColor(resources.getColor(R.color.red))
        }
        if (port.text.isNullOrEmpty()){
            port.setTextColor(resources.getColor(R.color.red))
        } else if (!Http.validatePort(port.text.toString().toInt())){
            port.setTextColor(resources.getColor(R.color.red))
        }
        if (    serverName.currentTextColor     == Color.RED    ||
                hostname.currentTextColor       == Color.RED    ||
                responseCode.currentTextColor   == Color.RED    ||
                port.currentTextColor           == Color.RED){
            return false
        }
        return true
    }

    /**
     * clear all fields
     */
    fun btnCLearClick(view: View) {
        serverName.text.clear()
        hostname.text.clear()
        responseCode.text.clear()
        protocol.setSelection(0)
    }
    /**
     * back to the main activity
     */
    fun btnBackClick(view: View) {
        onBackPressed()
    }
}
