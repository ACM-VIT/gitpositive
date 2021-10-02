package org.acmvit.gitpositive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import org.acmvit.gitpositive.databinding.ActivityMainBinding


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
