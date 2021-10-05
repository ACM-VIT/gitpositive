package org.acmvit.gitpositive

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.adapters.FollowersAdapter
import org.acmvit.gitpositive.adapters.FollowingAdapter
import org.acmvit.gitpositive.databinding.ActivityMainBinding
import org.acmvit.gitpositive.models.Follower
import org.acmvit.gitpositive.models.Following
import org.acmvit.gitpositive.repositoryList.ui.RepositoryActivity
import org.json.JSONObject

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
            followersBottomSheet()
            // Other Functionalities to be implemented.
        }

        binding.RepoCount.setOnClickListener{
            doVibration()
            // Other Functionalities to be implemented.
        }

        binding.FollowingCount.setOnClickListener{
            doVibration()
            followingBottomSheet()
            // Other Functionalities to be implemented.
        }

        viewModel.getUserData(intent.getStringExtra("Username").toString())
    }

    override fun onResume() {
        super.onResume()
        val noInternet = findViewById<ConstraintLayout>(R.id.noInternet)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->
            if(isConnected){
                noInternet.visibility = View.GONE
            } else {
                Toast.makeText(applicationContext, "Internet is not connected", Toast.LENGTH_SHORT).show()
                noInternet.visibility = View.VISIBLE
            }
        })
    }
    private fun followersBottomSheet(){
        val url = "https://api.github.com/users/"+intent.getStringExtra("Username").toString()+"/followers"
        var followersList = mutableListOf<Follower>()

        var layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(this)
        var adapter: RecyclerView.Adapter<FollowersAdapter.ViewHolder>? = FollowersAdapter(followersList)

        Log.i("url_of_profile", url)

        val followersDialog = BottomSheetDialog(this)
        followersDialog.setContentView(R.layout.bottom_followers)
        followersDialog.findViewById<TextView>(R.id.bottom_top)!!.text = Html.fromHtml(getColorStr("Your ", "#6CFF54") + getColorStr("Followers", getColor(R.color.text_color).toString()))
        val followersRecyclerView = followersDialog.findViewById<RecyclerView>(R.id.recyclerView)
        followersRecyclerView?.adapter = adapter
        followersRecyclerView?.layoutManager = layoutManager
        val mQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null
            , { response ->
                for(i in 0 until response.length()){
                    val follower: JSONObject = response.getJSONObject(i)
                    val login = follower["login"]
                    val id = follower["id"]
                    val node_id = follower["node_id"]
                    val avatar_url = follower["avatar_url"]
                    val gravatar_id = follower["gravatar_id"]
                    val url = follower["url"]
                    val html_url = follower["html_url"]
                    val followers_url = follower["followers_url"]
                    val following_url = follower["following_url"]
                    val gists_url = follower["gists_url"]
                    val starred_url = follower["starred_url"]
                    val subscriptions_url = follower["subscriptions_url"]
                    val organizations_url = follower["organizations_url"]
                    val repos_url = follower["repos_url"]
                    val events_url = follower["events_url"]
                    val received_events_url = follower["received_events_url"]
                    val type = follower["type"]
                    val site_admin = follower["site_admin"]

                    val followerData = Follower(
                        login as String,
                        id as Int,
                        node_id as String,
                        avatar_url as String,
                        gravatar_id as String,
                        url as String,
                        html_url as String,
                        followers_url as String,
                        following_url as String,
                        gists_url as String,
                        starred_url as String, subscriptions_url as String,
                        organizations_url as String,
                        repos_url as String, events_url as String,
                        received_events_url as String, type as String,
                        site_admin as Boolean
                    )
                    followersList.add(followerData)
                }
                adapter?.notifyDataSetChanged()

            }, { error ->
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            })
        mQueue.add(request)
        followersDialog.show()
    }

    private fun followingBottomSheet(){
        val url = "https://api.github.com/users/"+intent.getStringExtra("Username").toString()+"/following"
        var followingList = mutableListOf<Following>()

        var layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(this)
        var adapter: RecyclerView.Adapter<FollowingAdapter.ViewHolder>? = FollowingAdapter(followingList)

        Log.i("url_of_profile", url)

        val followingDialog = BottomSheetDialog(this)
        followingDialog.setContentView(R.layout.bottom_following)
        followingDialog.findViewById<TextView>(R.id.bottom_top)!!.text = Html.fromHtml(getColorStr("Your ", "#6CFF54") + getColorStr("Following", getColor(R.color.text_color).toString()))
        val followersRecyclerView = followingDialog.findViewById<RecyclerView>(R.id.recyclerView)
        followersRecyclerView?.adapter = adapter
        followersRecyclerView?.layoutManager = layoutManager
        val mQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null
            , { response ->
                for(i in 0 until response.length()){
                    val follower: JSONObject = response.getJSONObject(i)
                    val login = follower["login"]
                    val id = follower["id"]
                    val node_id = follower["node_id"]
                    val avatar_url = follower["avatar_url"]
                    val gravatar_id = follower["gravatar_id"]
                    val url = follower["url"]
                    val html_url = follower["html_url"]
                    val followers_url = follower["followers_url"]
                    val following_url = follower["following_url"]
                    val gists_url = follower["gists_url"]
                    val starred_url = follower["starred_url"]
                    val subscriptions_url = follower["subscriptions_url"]
                    val organizations_url = follower["organizations_url"]
                    val repos_url = follower["repos_url"]
                    val events_url = follower["events_url"]
                    val received_events_url = follower["received_events_url"]
                    val type = follower["type"]
                    val site_admin = follower["site_admin"]

                    val following = Following(
                        login as String,
                        id as Int,
                        node_id as String,
                        avatar_url as String,
                        gravatar_id as String,
                        url as String,
                        html_url as String,
                        followers_url as String,
                        following_url as String,
                        gists_url as String,
                        starred_url as String, subscriptions_url as String,
                        organizations_url as String,
                        repos_url as String, events_url as String,
                        received_events_url as String, type as String,
                        site_admin as Boolean
                    )
                    followingList.add(following)
                }
                adapter?.notifyDataSetChanged()

            }, { error ->
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            })
        mQueue.add(request)
        followingDialog.show()
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
