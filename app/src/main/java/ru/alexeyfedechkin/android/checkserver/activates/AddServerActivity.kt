package ru.alexeyfedechkin.android.checkserver.activates

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.R
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.HOSTNAME_KEY
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.IS_USE_DEFAULT_PORT_KEY
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.PORT_KEY
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.PROTOCOL_KEY
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.RESPONSE_CODE_KEY
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.SERVER_NAME_KEY
import ru.alexeyfedechkin.android.checkserver.SavingKey.Companion.SERVER_NAME_TO_EDIT_KEY
import ru.alexeyfedechkin.android.checkserver.enums.Protocol
import ru.alexeyfedechkin.android.checkserver.models.Server
import ru.alexeyfedechkin.android.checkserver.network.Http
import ru.alexeyfedechkin.android.checkserver.network.Net

class EditServerActivity : AppCompatActivity() {
    private var sourceServer = Server()
    private val serverName: EditText by lazy  {
        findViewById<EditText>(R.id.editText_serverName)
    }

    private val hostname: EditText by lazy {
        findViewById<EditText>(R.id.editText_hostname)
    }

    private val responseCode: EditText by lazy{
        findViewById<EditText>(R.id.editText_responseCode)
    }
    private val port: EditText by lazy {
        findViewById<EditText>(R.id.editText_port)
    }
    private val protocol: Spinner by lazy {
        findViewById<Spinner>(R.id.spinner_protocol)
    }
    private val isUseDefaultPortCheckbox: CheckBox by lazy {
        findViewById<CheckBox>(R.id.checkBox_isUserDefaultPort)
    }

    private val isUseDefaultPort:Boolean
        get() {
            return isUseDefaultPortCheckbox.isChecked
        }

    private val db: DB = DB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_server)
        db.init(applicationContext)

        serverName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    serverName.setTextColor(resources.getColor(R.color.black))
                }
            }
        })

        hostname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    hostname.setTextColor(resources.getColor(R.color.black))
                }
            }
        })

        responseCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    responseCode.setTextColor(resources.getColor(R.color.black))
                }
            }
        })
        port.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    port.setTextColor(resources.getColor(R.color.black))
                }
            }
        })
        protocol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isUseDefaultPort){
                    port.setText(Protocol.values()[position].defaultPort.toString())
                }
            }
        }
        isUseDefaultPortCheckbox.setOnClickListener  { view ->
            port.isEnabled = !view.findViewById<CheckBox>(R.id.checkBox_isUserDefaultPort).isChecked
            val foundProtocol = Protocol.isDefaultPort(port.text.toString().toInt())
            if (foundProtocol != null){
                protocol.setSelection(foundProtocol.position)
            } else {
                port.setText(Protocol.values()[protocol.selectedItemPosition].defaultPort.toString())
            }
        }

        val arrayAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item)
        for (protocol in Protocol.values()){
            arrayAdapter.add(protocol.protocol)
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val protocolSpinner = findViewById<Spinner>(R.id.spinner_protocol)
        protocolSpinner.adapter = arrayAdapter

        val bundle = intent.extras
        sourceServer = db.getServerByName(bundle?.getString(SERVER_NAME_TO_EDIT_KEY) as String)
        fillData(sourceServer)
    }

    private fun fillData(server: Server){
        serverName.setText(server.name)
        hostname.setText(server.hostname)
        responseCode.setText(server.responseCode.toString())
        port.setText(server.port.toString())
        val foundProtocol = Protocol.isDefaultPort(port.text.toString().toInt())
        if (foundProtocol != null){
            protocol.setSelection(foundProtocol.position)
        } else {
            port.setText(Protocol.values()[protocol.selectedItemPosition].defaultPort.toString())
        }
    }

    /**
     *
     */
    override fun onBackPressed() {
        finish()
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
        outState.putBoolean(IS_USE_DEFAULT_PORT_KEY, isUseDefaultPort)
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
        isUseDefaultPortCheckbox.isChecked = savedInstanceState.getBoolean(IS_USE_DEFAULT_PORT_KEY)
    }

    fun btnSaveClick(view: View) {
        val server = Server()
        if (!validateData()){
            return
        }
        server.name         = serverName.text.toString()
        server.hostname     = hostname.text.toString()
        server.responseCode = responseCode.text.toString().toInt()
        server.port         = port.text.toString().toInt()
        server.protocol     = Protocol.values()[protocol.selectedItemPosition]
        try {
            db.updateServer(server, sourceServer)
            finish()
        } catch (ex: RealmPrimaryKeyConstraintException){
            Toast.makeText(applicationContext, resources.getText(R.string.nameAlreadyExist), Toast.LENGTH_SHORT).show()
            serverName.setTextColor(resources.getColor(R.color.red))
        }
    }
    fun btnBackClick(view: View) {
        finish()
    }
    fun btnCheckClick(view: View) {
        if (!validateData()){
            return
        }
        val server = Server()
        server.name         = serverName.text.toString()
        server.hostname     = hostname.text.toString()
        server.responseCode = responseCode.text.toString().toInt()
        server.port         = port.text.toString().toInt()
        server.protocol     = Protocol.values()[protocol.selectedItemPosition]
        doAsync {
            val responseCode = Net.checkServerResponse(server)
            var rc = resources.getString(R.string.server_is_not_available)
            if (responseCode != -1){
                rc = responseCode.toString()
            }
            uiThread {
                Toast.makeText(this@EditServerActivity,
                    "${resources.getString(R.string.expectedCode)} " +
                            "${server.responseCode} \n ${resources.getString(R.string.actualCode)}" +
                            " $rc", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * check text from field for correctness
     * if data invalid set text color to red
     * @return true if alla fields filled and correct
     */
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

        }
        if (port.text.isNullOrEmpty()){
            port.setTextColor(resources.getColor(R.color.red))
        } else if (!Http.validatePort(port.text.toString().toInt())){
            port.setTextColor(resources.getColor(R.color.red))
        }
        if (serverName.currentTextColor     == Color.RED    ||
            hostname.currentTextColor       == Color.RED    ||
            responseCode.currentTextColor   == Color.RED    ||
            port.currentTextColor           == Color.RED){
            return false
        }
        return true
    }
}