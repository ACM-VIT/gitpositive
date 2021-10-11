package org.acmvit.gitpositive

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.view.View
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.LayoutInflater
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.databinding.ActivityHomeScreenBinding


@AndroidEntryPoint
class HomeScreen : AppCompatActivity() {
    var vibrator: Vibrator? = null

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: ActivityHomeScreenBinding? = null

    private val binding: ActivityHomeScreenBinding
        get() {
            return _binding!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()
        binding.Appname.text = Html.fromHtml(
            getColorStr("Git", "#6CFF54") + getColorStr(
                "Positive",
                getColor(R.color.text_color).toString()
            )
        )

        binding.username.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.getFilterData(p0.toString())
            }

        })
        binding.username.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
//               Hide the soft keyboard
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                    hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                }
                binding.username.clearFocus()
                binding.username.isCursorVisible = false
                startMainActivity()
                return@OnKeyListener true
            }
            false
        })

        binding.floatingActionButton.setOnClickListener {
            startMainActivity()
        }
    }

    private fun observeViewModel() {
        viewModel.userList.observe(this) {
            it?.let { usersList ->
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_dropdown_item_1line, usersList
                )
                binding.username.setAdapter(adapter)
            }
        }
    }

    private fun doVibration() {
        vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator!!.vibrate(
            VibrationEffect.createOneShot(
                50,
                VibrationEffect.EFFECT_TICK
            )
        )
    }
    private fun startMainActivity(){
        doVibration()
        if (binding.username.text.toString().isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Username", binding.username.text?.toString())
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, "Username cannot be empty!!", Toast.LENGTH_SHORT)
                .show()
        }
    }
    override fun onResume() {
        super.onResume()
        val noInternet = findViewById<ConstraintLayout>(R.id.noInternet)
        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val textBox = findViewById<TextInputLayout>(R.id.textInputLayout)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                noInternet.visibility = View.GONE
                fab.visibility = View.VISIBLE
                textBox.visibility = View.VISIBLE
            } else {
                Toast.makeText(applicationContext, "Internet is not connected", Toast.LENGTH_SHORT)
                    .show()
                noInternet.visibility = View.VISIBLE
                fab.visibility = View.GONE
                textBox.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val noButton = dialogView.findViewById<Button>(R.id.no)
        val yesButton = dialogView.findViewById<Button>(R.id.yes)
        val exitDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        exitDialog.show()
        exitDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        noButton.setOnClickListener{
            exitDialog.dismiss()
        }
        yesButton.setOnClickListener{
            finish()
        }
        exitDialog.show()
    }
}


fun getColorStr(text: String, color: String): String = "<font color=$color>$text</font>"