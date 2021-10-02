package org.acmvit.gitpositive
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.databinding.ActivityHomeScreenBinding


@AndroidEntryPoint
class HomeScreen : AppCompatActivity() {
    var vibrator: Vibrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.Appname.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", getColor(R.color.text_color).toString()))

        val button = binding.floatingActionButton
        button.setOnClickListener {
            doVibration()
            if(binding.username.text.toString().isNotEmpty()){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Username",binding.username.text?.toString())
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,"Username cannot be empty!!",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun doVibration() {
            vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator!!.vibrate(
                VibrationEffect.createOneShot(
                    50,
                    VibrationEffect.EFFECT_TICK
                )
            )
    }
}


fun getColorStr (text : String, color : String): String  =  "<font color=$color>$text</font>"