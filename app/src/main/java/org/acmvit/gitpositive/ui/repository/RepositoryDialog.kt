package org.acmvit.gitpositive.ui.repository

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
import org.acmvit.gitpositive.remote.model.Repository
import org.acmvit.gitpositive.util.getColorStr
import org.json.JSONObject

class RepositoryDialog(context: Context, userName: String) : BottomSheetDialog(context) {

    val url = "https://api.github.com/users/$userName/repos?per_page=100"
    var repositoryList = mutableListOf<Repository>()

    private var layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(this.context)
    var adapter: RecyclerView.Adapter<RepositoryAdapter.ViewHolder>? =
        RepositoryAdapter(repositoryList)


    init {
        setContentView(R.layout.bottom_followers)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.bottom_top)!!.text = Html.fromHtml(
            getColorStr("Your ", "#6CFF54") + getColorStr(
                "Repositories",
                this.context.getColor(R.color.text_color).toString()
            )
        )
        val repoRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        repoRecyclerView?.adapter = adapter
        repoRecyclerView?.layoutManager = layoutManager
        val mQueue = Volley.newRequestQueue(this.context)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null, { response ->
                for (i in 0 until response.length()) {
                    val repo: JSONObject = response.getJSONObject(i)
                    val name = repo["name"].toString()
                    val html_url = repo["html_url"].toString()
                    val description = repo["description"].toString()
                    val language = repo["language"].toString()
                    val stars = repo["stargazers_count"]
                    val forks_count = repo["forks_count"]

                    val repoData = Repository(
                        name,
                        html_url,
                        description,
                        language,
                        stars as Int, forks_count as Int
                    )
                    repositoryList.add(repoData)
                }
                adapter?.notifyDataSetChanged()

            }, { error ->
                Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
            })
        mQueue.add(request)
    }

}