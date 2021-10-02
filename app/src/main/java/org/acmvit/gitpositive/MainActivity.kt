package org.acmvit.gitpositive

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import org.acmvit.gitpositive.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var BaseURL="https://api.github.com/"
class MainActivity : AppCompatActivity() {
    private var loadingDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMainBinding.inflate(layoutInflater)
        val view = Binding.root
        setContentView(view)
        showLoadingDialog()
        getUserData(binding = Binding);
    }

        fun getUserData(binding:ActivityMainBinding) {
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseURL)
                .build()
                .create(ApiInterface::class.java)
            val username= intent.getStringExtra("Username").toString()
            binding.appName.text = Html.fromHtml(getColorStr("Git", "#6CFF54") + getColorStr("Positive",
                getColor(R.color.text_color).toString()
            ))
            val retrofitData = retrofitBuilder.getData(username)
            retrofitData.enqueue(object : Callback<UserData?> {
                override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                    val responseBody = response.body()
                    binding.FollowingCount.append(responseBody?.following.toString())
                    binding.followerCount.append( responseBody?.followers.toString())
                    binding.RepoCount.append(responseBody?.public_repos.toString())
                    binding.bio.text=responseBody?.bio
                    binding.username.text=responseBody?.name
                    binding.userName.text = responseBody?.login
                    Glide.with(this@MainActivity)
                        .load(responseBody?.avatar_url)
                        .error(R.drawable.error1)
                        .override(200, 200)
                        .centerCrop()
                        .into(binding.avatar)
                    hideLoadingDialog()
                }

                override fun onFailure(call: Call<UserData?>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message, Toast.LENGTH_SHORT).show()
                    hideLoadingDialog()
                }
            })
        }
    fun showLoadingDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        loadingDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        loadingDialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog!!.show()
    }
    fun hideLoadingDialog(){
        loadingDialog!!.hide()
    }
}
