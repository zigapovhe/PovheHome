package si.povhe.povhehome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executors

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        BiometricManager()

        retryBiometricBtn.setOnClickListener {
            BiometricManager()
        }
    }

    fun sendToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    fun BiometricManager(){
        val executor = Executors.newSingleThreadExecutor()
        val activity: FragmentActivity = this // reference to activity
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        // user clicked negative button
                    } else {
                        TODO("Called when an unrecoverable error has been encountered and the operation is complete.")
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    //Called when a biometric is recognized.
                    val startPovheHome = Intent(activity,MainActivity::class.java)
                    startActivity(startPovheHome)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    //Called when a biometric is valid but not recognized.
                    sendToast("Auth failed")
                    //BiometricManager()
                }
            })

        val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("PovheHome's Fingerprint Auth")
            .setDescription("Please touch the fingerprint reader to authenticate")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }


}
