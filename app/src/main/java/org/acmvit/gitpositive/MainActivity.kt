package org.acmvit.gitpositive

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.acmvit.gitpositive.databinding.ActivityMainBinding
import org.acmvit.gitpositive.repositoryList.ui.RepositoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var BaseURL = "https://api.github.com/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMainBinding.inflate(layoutInflater)
        val view = Binding.root
        setContentView(view)
        getUserData(binding = Binding);
    }

    fun getUserData(binding: ActivityMainBinding) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BaseURL)
            .build()
            .create(ApiInterface::class.java)
        val username = intent.getStringExtra("Username").toString()
        binding.appName.text =
            Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive", "#FFFFFF"))
        binding.repositoryButton.setOnClickListener {
            val intent = Intent(this, RepositoryActivity::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }
        val retrofitData = retrofitBuilder.getData(username)
        retrofitData.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                val responseBody = response.body()
                binding.FollowingCount.append(responseBody?.following.toString())
                binding.followerCount.append(responseBody?.followers.toString())
                binding.RepoCount.append(responseBody?.public_repos.toString())
                binding.bio.text = responseBody?.bio
                binding.username.text = responseBody?.name
                binding.userName.text = responseBody?.login
                Glide.with(this@MainActivity)
                    .load(responseBody?.avatar_url)
                    .error(R.drawable.error1)
                    .override(200, 200)
                    .centerCrop()
                    .into(binding.avatar)
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {

            }
        })
    }
}
