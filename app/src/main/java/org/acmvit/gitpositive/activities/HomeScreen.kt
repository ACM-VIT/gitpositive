package org.acmvit.gitpositive.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.databinding.ActivityHomeScreenBinding
import org.acmvit.gitpositive.network.NetworkConnection


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

    override fun onResume() {
        super.onResume()
        val noInternet = findViewById<ConstraintLayout>(R.id.noInternet)
        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val textBox = findViewById<TextInputLayout>(R.id.textInputLayout)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->
            if(isConnected){
                noInternet.visibility = View.GONE
                fab.visibility = View.VISIBLE
                textBox.visibility = View.VISIBLE
            } else {
                Toast.makeText(applicationContext, "Internet is not connected", Toast.LENGTH_SHORT).show()
                noInternet.visibility = View.VISIBLE
                fab.visibility = View.GONE
                textBox.visibility = View.GONE
            }
        })
    }
}


fun getColorStr (text : String, color : String): String  =  "<font color=$color>$text</font>"