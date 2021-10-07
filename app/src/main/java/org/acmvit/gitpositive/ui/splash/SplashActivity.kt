package org.acmvit.gitpositive.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.widget.TextView
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.ui.home.HomeScreen
import org.acmvit.gitpositive.ui.home.getColorStr

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }, 3000)
        val appName = findViewById<TextView>(R.id.appName2)
        appName.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", getColor(
            R.color.text_color
        ).toString()))
    }
}