package org.acmvit.gitpositive.ui.following

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.remote.model.Following
import org.acmvit.gitpositive.util.getColorStr
import org.json.JSONObject


class FollowingDialog(context: Context, userName: String) : BottomSheetDialog(context) {

    private val url = "https://api.github.com/users/$userName/following?per_page=100"

    var followingList = mutableListOf<Following>()
    var layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(this.context)
    var adapter: RecyclerView.Adapter<FollowingAdapter.ViewHolder>? =
        FollowingAdapter(followingList)


    init {
        setContentView(R.layout.bottom_following)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<TextView>(R.id.bottom_top)!!.text = Html.fromHtml(
            getColorStr("Your ", "#6CFF54") + getColorStr(
                "Following", this.context.getColor(
                    R.color.text_color
                ).toString()
            )
        )
        val followersRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        followersRecyclerView?.adapter = adapter
        followersRecyclerView?.layoutManager = layoutManager
        val mQueue = Volley.newRequestQueue(this.context)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null, { response ->
                for (i in 0 until response.length()) {
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
                Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
            })
        mQueue.add(request)
    }
}