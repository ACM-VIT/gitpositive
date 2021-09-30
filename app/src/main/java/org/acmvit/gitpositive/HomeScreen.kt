package org.acmvit.gitpositive
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import org.acmvit.gitpositive.databinding.ActivityHomeScreenBinding
import android.content.Intent


class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.Appname.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", "#FFFFFF"))

        val button = binding.floatingActionButton
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Username",binding.username.text?.toString())
            startActivity(intent)
        }
    }
}


fun getColorStr (text : String, color : String): String  =  "<font color=$color>$text</font>"