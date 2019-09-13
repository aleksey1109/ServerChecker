package ru.alexeyfedechkin.android.checkserver.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm
import ru.alexeyfedechkin.android.checkserver.DB
import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.R

class AddServerActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "AddServerActivity"
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
    private val db:DB = DB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)
        db.init(applicationContext)
    }

    /**
     *
     */
    override fun onBackPressed() {
        super.onBackPressed()

    }

    /**
     *
     * @param view
     */
    fun btnSaveClick(view: View) {
        val server = Server()
        server.name = serverName.text.toString()
        server.hostname = hostname.text.toString()
        server.responseCode = responseCode.text.toString().toInt()
        db.realm.executeTransactionAsync({
             fun execute(bgRealm: Realm){
                bgRealm.beginTransaction()
                bgRealm.insert(server)
                bgRealm.commitTransaction()
                Log.i(TAG, "save new server")
            }
        }, {
            fun onSuccess(){
                Toast.makeText(applicationContext, resources.getText(R.string.saveSucces), Toast.LENGTH_SHORT)
            }
        }, {
            fun onError(){
                Toast.makeText(applicationContext, resources.getText(R.string.saveFail), Toast.LENGTH_SHORT)
            }
        })
    }
    /**
     * clear all fields
     * @param view
     */
    fun btnCLearClick(view: View) {
        serverName.text.clear()
        hostname.text.clear()
        responseCode.text.clear()
    }
    /**
     * back to the main activity
     * @param view
     */
    fun btnBackClick(view: View) {
        onBackPressed()
    }
}
