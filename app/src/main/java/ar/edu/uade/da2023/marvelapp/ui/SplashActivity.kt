package ar.edu.uade.da2023.marvelapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ar.edu.uade.da2023.marvelapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            // Con esto hago que vaya a la pantalla de MainActivity
            var intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            finish()

        },2000)
    }
}