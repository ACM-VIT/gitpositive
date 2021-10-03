package org.acmvit.gitpositive

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.databinding.ActivityMainBinding
import org.acmvit.gitpositive.repositoryList.ui.RepositoryActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel by viewModels<MainViewModel>()
    private var _binding: ActivityMainBinding? = null
    var vibrator: Vibrator? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding!!
        }

    private val loadingView by lazy {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        val view = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        view.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return@lazy view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoadingDialog()
        observeViewState()
        binding.appName.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", getColor(R.color.text_color).toString()))

        binding.followerCount.setOnClickListener{
            doVibration()
            // Other Functionalities to be implemented.
        }

        binding.RepoCount.setOnClickListener{
            doVibration()
            // Other Functionalities to be implemented.
        }

        binding.FollowingCount.setOnClickListener{
            doVibration()
            // Other Functionalities to be implemented.
        }

        viewModel.getUserData(intent.getStringExtra("Username").toString())
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this) {
            it?.let { viewState ->
                when (viewState) {
                    is MainViewModel.ViewState.Error -> {
                        showError(viewState.message)
                        hideLoadingDialog()
                    }
                    MainViewModel.ViewState.Loading -> {
                        showLoadingDialog()
                    }
                    is MainViewModel.ViewState.Success -> {
                        showData(viewState.userData)
                        hideLoadingDialog()
                    }
                }
            }
        }
    }

    private fun showData(userData: UserData) {
        with(binding) {
            FollowingCount.append(userData.following.toString())
            followerCount.append(userData.followers.toString())
            RepoCount.append(userData.public_repos.toString())
            bio.text = userData.bio
            username.text = userData.name
            userName.text = userData.login
            repositoryButton.setOnClickListener {
                doVibration()
                val intent = Intent(this@MainActivity, RepositoryActivity::class.java)
                intent.putExtra("Username", userData.login)
                startActivity(intent)
            }
            Glide.with(this@MainActivity)
                .load(userData.avatar_url)
                .error(R.drawable.error1)
                .override(200, 200)
                .centerCrop()
                .into(binding.avatar)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoadingDialog() {
        loadingView.show()
    }

    private fun hideLoadingDialog() {
        loadingView.hide()
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
