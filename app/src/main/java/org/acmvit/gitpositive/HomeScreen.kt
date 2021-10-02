package org.acmvit.gitpositive
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.content.Intent
import android.widget.Toast
import org.acmvit.gitpositive.databinding.ActivityHomeScreenBinding


class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.Appname.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", getColor(R.color.text_color).toString()))

        val button = binding.floatingActionButton
        button.setOnClickListener {
            if(binding.username.text.toString().isNotEmpty()){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Username",binding.username.text?.toString())
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,"Username cannot be empty!!",Toast.LENGTH_SHORT).show()
            }

        }
    }
}


fun getColorStr (text : String, color : String): String  =  "<font color=$color>$text</font>"