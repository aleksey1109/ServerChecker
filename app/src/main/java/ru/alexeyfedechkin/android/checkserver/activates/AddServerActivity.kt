package ru.alexeyfedechkin.android.checkserver.activates

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import ru.alexeyfedechkin.android.checkserver.enums.Protocol
import ru.alexeyfedechkin.android.checkserver.models.Server
import ru.alexeyfedechkin.android.checkserver.network.Http
import ru.alexeyfedechkin.android.checkserver.network.Net

class AddServerActivity : AppCompatActivity() {

    private val serverNameEditText:EditText by lazy  {
        findViewById<EditText>(R.id.editText_serverName)
    }

    private val hostnameEditText:EditText by lazy {
        findViewById<EditText>(R.id.editText_hostname)
    }

    private val responseCodeEditText:EditText by lazy{
        findViewById<EditText>(R.id.editText_responseCode)
    }

    private val portEditText:EditText by lazy {
        findViewById<EditText>(R.id.editText_port)
    }

    private val protocolSpinner:Spinner by lazy {
        findViewById<Spinner>(R.id.spinner_protocol)
    }

    private val isUseDefaultPortCheckbox:CheckBox by lazy {
        findViewById<CheckBox>(R.id.checkBox_isUseDefaultPort)
    }

    //можно было и не указывать тип но хз скомпилится ли
    private val db:DB = DB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)
        db.init(applicationContext)
        serverName.addTextChangedListener(object : TextWatcher{
            //все аргументы правильные. -> google dev doc
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (serverName.currentTextColor == Color.RED){
                    serverName.setTextColor(resources.getColor(R.color.black))
                }
            }
        }
        )


        private const val EXIT_DELAY_DURATION = 2000
        private var backPressedTimestamp:Long = 0
        /**
         *  if one field is is not empty displays a warning Toast about exit without saving data.
         *  shown when pressed for less than 2 seconds
         */
        override fun onBackPressed() {
            if (serverName.text.isNotEmpty()    ||
                hostname.text.isNotEmpty()      ||
                responseCode.text.isNotEmpty()) {

                if (backPressedTimestamp + EXIT_DELAY_DURATION > System.currentTimeMillis()){
                    super.onBackPressed()
                }
                else{
                    Toast.
                        makeText(this,resources.getString(R.string.AddBackAlertMessage), Toast.LENGTH_SHORT).
                        show()
                }
                backPressedTimestamp = System.currentTimeMillis()
            } else {
                super.onBackPressed()
            }
        }


        /**
         * save data in database
         */
        fun btnSaveClick(view: View) {
            val server = Server()

            if (!validateData()) return;

            server.name         = serverNameEditText.text.toString()
            server.hostname     = hostnameEditText.text.toString()
            server.responseCode = responseCodeEditText.text.toString().toInt()
            server.port         = portEditText.text.toString().toInt()
            server.protocol     = protocolSpinner.values()[protocol.selectedItemPosition]

            try {
                db.saveServer(server)
                finish()
            } catch (ex: RealmPrimaryKeyConstraintException){
                Toast.
                    makeText(applicationContext, resources.getText(R.string.nameAlreadyExist), Toast.LENGTH_SHORT).
                    show()
                serverName.setTextColor(resources.getColor(R.color.red))
            }
        }

        /**
         * check text from field for correctness
         * if data invalid set text color to red
         * @return true if all fields filled and correct
         */
        private fun validateData():Boolean{
            if (serverNameEditText.text.isNullOrEmpty()){
                serverNameEditText.setTextColor(resources.getColor(R.color.red))
            }
            val url = "${Protocol.values()[protocol.selectedItemPosition].protocol}://${hostname.text}";
            if (hostnameEditText.text.isNullOrEmpty() ||
                !URLUtil.ValidateUrl(url)){
                hostnameEditText.setTextColor(resources.getColor(R.color.red))
            }
            if (responseCodeEditText.text.isNullOrEmpty() ||
                !Http.validateResponseCode(responseCode.text.toString().toInt())){
                responseCodeEditText.setTextColor(resources.getColor(R.color.red))
            }
            if (portEditText.text.isNullOrEmpty() ||
                !Http.validatePort(portEditText.text.toString().toInt())){
                portEditText.setTextColor(resources.getColor(R.color.red))
            }
            if (serverNameEditText.currentTextColor     == Color.RED    ||
                hostnameEditText.currentTextColor       == Color.RED    ||
                responseCodeEditText.currentTextColor   == Color.RED    ||
                portEditText.currentTextColor           == Color.RED){
                return false
            }
            return true
        }

        /**
         * clear all fields
         */
        fun btnCLearClick(view: View) {
            serverNameEditText.text.clear()
            hostnameEditText.text.clear()
            responseCodeEditText.text.clear()
            protocolSpinner.setSelection(0)
            portEditText.setText(Protocol.values()[0].defaultPort.toString())
        }

        /**
         * back to the main activity
         */
        fun btnBackClick(view: View) {
            finish()
        }

        /**
         * test server response
         * @param view
         */
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
                    Toast.makeText(this@AddServerActivity,
                        "${resources.getString(R.string.expectedCode)} " +
                                "${server.responseCode} \n ${resources.getString(R.string.actualCode)}" +
                                " $rc", Toast.LENGTH_LONG).show()
                }
            }
        }
}