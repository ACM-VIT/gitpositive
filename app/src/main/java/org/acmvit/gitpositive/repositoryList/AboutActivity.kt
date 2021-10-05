package org.acmvit.gitpositive.repositoryList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.getColorStr

class AboutActivity : AppCompatActivity() {
    val message = "Hey!! I found an amazing app called GitPositive created by ACM-VIT. " +
            "I recommend you to download it. " +
            "It helps you to view you GitHub profile in an efficient manner. "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val appName = findViewById<TextView>(R.id.appname)
        val backFab = findViewById<FloatingActionButton>(R.id.back)
        val shareFab = findViewById<FloatingActionButton>(R.id.share)
        appName.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", getColor(R.color.text_color).toString()))
        backFab.setOnClickListener({
            onBackPressed()
        })
        shareFab.setOnClickListener({
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "GitPositive"))
        })

    }
}