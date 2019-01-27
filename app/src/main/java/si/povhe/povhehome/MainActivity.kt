package si.povhe.povhehome

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import si.povhe.povhehome.Services.AuthService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lightBtn.setOnClickListener {
            turnOnTheLight(this, "FkxYF#@!Zb25H5k"){ complete ->
                if (complete) {

                }
            }
        }

        zigapcBtn.setOnClickListener {
            turnZigaPC(this)
        }
    }

    fun turnOnTheLight(context: Context, auth:String, complete: (Boolean) -> Unit){
        val url= URL_LIGHT

        val jsonBody = JSONObject()
        jsonBody.put("auth", auth)
        val requestBody = jsonBody.toString()

        val lightRequest = object : StringRequest(Method.POST, URL_LIGHT, Response.Listener { response ->
            Toast.makeText(this, "$response", Toast.LENGTH_SHORT).show()
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            Log.d("ERROR", "Could not switch the light: $error")
            complete(false)
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
        val url= URL_ZigaPC

        val jsonBody = JSONObject()

        val requestBody = jsonBody.toString()

        val zigaPCRequest = object : StringRequest(Method.GET, URL_ZigaPC, Response.Listener { response ->
            Toast.makeText(this, "$response", Toast.LENGTH_SHORT).show()
            println(response)
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
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

}