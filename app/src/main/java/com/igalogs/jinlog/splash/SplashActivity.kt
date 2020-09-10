package com.igalogs.jinlog.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.igalogs.jinlog.R
import com.igalogs.jinlog.login.LoginActivity
import com.igalogs.jinlog.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private var handler: Handler = Handler()

    private var runnnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        runnnable = Runnable {
            routingWithLoginStatus()
        }

        handler.postDelayed(runnnable, SPLASH_DISPLAY_TIME)
    }

    override fun onPause() {
        super.onPause()
        if (runnnable != null) {
            handler.removeCallbacks(runnnable)
        }
    }

    private fun routingWithLoginStatus() {
        var intent: Intent
        if (auth.currentUser != null) {
            intent = Intent(this, HomeActivity::class.java)
        } else {
            intent = Intent(this,LoginActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    companion object {
        const val SPLASH_DISPLAY_TIME = 300L
    }


}