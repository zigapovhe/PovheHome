package si.povhe.povhehome

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val channelID = "RPI3_Channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        versionTxt.text = "v"+BuildConfig.VERSION_NAME

        //Ce je SDK vecji od 26, nizje verzije androida bodo notification channel ignorirale
        createNotificationChannel()

        lightBtn.setOnClickListener {
            turnOnTheLight(this, light_key)
        }

        zigapcBtn.setOnClickListener {
            turnZigaPC(this)
        }
    }

    fun turnOnTheLight(context: Context, auth:String){
        val jsonBody = JSONObject()
        jsonBody.put("auth", auth)
        val requestBody = jsonBody.toString()

        val lightRequest = object : StringRequest(Method.POST, URL_LIGHT, Response.Listener { response ->
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            //println(response)
            sendNotification("LightSwitch",response)
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            sendNotification("LightSwitch","Could not switch the light: $error")
            Log.d("ERROR", "Could not switch the light: $error")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(lightRequest)
    }

    fun turnZigaPC(context: Context){
        val jsonBody = JSONObject()

        val requestBody = jsonBody.toString()

        val zigaPCRequest = object : StringRequest(Method.GET, URL_ZigaPC, Response.Listener { response ->
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            sendNotification("ZigaPC", response)
            println(response)
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            sendNotification("ZigaPC", "Could not turn on zigapc: $error")
            Log.d("ERROR", "Could not turn on zigapc: $error")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(zigaPCRequest)
    }


    //Notifications
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title:String, content:String) {
        var notificationID = 1

        val builder = Notification.Builder(this, channelID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.mipmap.icon)
            .setChannelId(channelID)


        with(NotificationManagerCompat.from(this)) {
            notify(notificationID++, builder.build())
        }


    }

}

