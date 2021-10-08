package org.acmvit.gitpositive.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.util.NetworkConnection
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.remote.model.UserData
import org.acmvit.gitpositive.databinding.ActivityMainBinding
import org.acmvit.gitpositive.ui.follower.FollowerDialog
import org.acmvit.gitpositive.ui.following.FollowingDialog
import org.acmvit.gitpositive.ui.repository.RepositoryActivity
import org.acmvit.gitpositive.ui.repository.RepositoryDialog
import org.acmvit.gitpositive.util.doVibration
import org.acmvit.gitpositive.util.getColorStr

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private var _binding: ActivityMainBinding? = null
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
        binding.appName.text = Html.fromHtml(
            getColorStr("Git", "#6CFF54") + getColorStr(
                "Positive", getColor(
                    R.color.text_color
                ).toString()
            )
        )

        binding.followerCount.setOnClickListener {
            doVibration()
            followersBottomSheet()
            // Other Functionalities to be implemented.
        }

        binding.RepoCount.setOnClickListener {
            doVibration()
            repositoriesBottomSheet()
            // Other Functionalities to be implemented.
        }

        binding.FollowingCount.setOnClickListener {
            doVibration()
            followingBottomSheet()
            // Other Functionalities to be implemented.
        }

        binding.shareProfile.setOnClickListener {
            doVibration()
            val message = "Hey!! Follow me on GitHub with the given link " +
                    "and don't forget to star my repositories \n" +
                    "https://github.com/" + intent.getStringExtra("Username").toString()
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "GitPositive"))
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
            doVibration()
        }

        viewModel.getUserData(intent.getStringExtra("Username").toString())
    }

    override fun onResume() {
        super.onResume()
        val noInternet = findViewById<ConstraintLayout>(R.id.noInternet)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                noInternet.visibility = View.GONE
            } else {
                Toast.makeText(applicationContext, "Internet is not connected", Toast.LENGTH_SHORT)
                    .show()
                noInternet.visibility = View.VISIBLE
            }
        }
    }

    private fun followersBottomSheet() {
        val followerDialog = FollowerDialog(this, intent.getStringExtra("Username").orEmpty())
        followerDialog.show()
    }

    private fun followingBottomSheet() {
        val followingDialog = FollowingDialog(this, intent.getStringExtra("Username").orEmpty())
        followingDialog.show()
    }

    private fun repositoriesBottomSheet() {
        val repoDialog = RepositoryDialog(this, intent.getStringExtra("Username").orEmpty())
        repoDialog.show()
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
                this@MainActivity.doVibration()
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
}
