package org.acmvit.gitpositive.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.models.Follower

class FollowersAdapter(private val dataSet: MutableList<Follower>) :
    RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.name)
        val image : ImageView = view.findViewById(R.id.userImage)
        val clickMe : CardView = view.findViewById(R.id.clickMe)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.follower_following_layout, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet.elementAt(position).login
        Glide.with(viewHolder.image.context)
            .load(dataSet.elementAt(position).avatar_url)
            .error(R.drawable.error1)
            .override(200, 200)
            .centerCrop()
            .into(viewHolder.image)
        viewHolder.clickMe.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(dataSet.elementAt(position).html_url))
            viewHolder.clickMe.context.startActivity(browserIntent)
        }
    }
    override fun getItemCount() = dataSet.size

}
