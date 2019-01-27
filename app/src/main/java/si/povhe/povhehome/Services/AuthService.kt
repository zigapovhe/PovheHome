package si.povhe.povhehome.Services

import android.app.PendingIntent.getActivity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import si.povhe.povhehome.MainActivity
import si.povhe.povhehome.URL_LIGHT
import si.povhe.povhehome.URL_ZigaPC


object AuthService {


    fun turnOnTheLight(context: Context, auth:String, complete: (Boolean) -> Unit){
        val url= URL_LIGHT

        val jsonBody = JSONObject()
        jsonBody.put("auth", auth)
        val requestBody = jsonBody.toString()

        val lightRequest = object : StringRequest(Method.POST, URL_LIGHT, Response.Listener { response ->
            Toast.makeText(MainActivity(), "$response", Toast.LENGTH_SHORT).show()
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            //Toast.makeText(MainActivity(), "$error", Toast.LENGTH_SHORT).show()
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

    fun turnOnZigaPC(){

        val stringRequest = StringRequest(
            Request.Method.GET, URL_ZigaPC,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                println( "Response is: ${response.substring(0, 500)}")
            },
            Response.ErrorListener { println( "That didn't work!") })


    }

}