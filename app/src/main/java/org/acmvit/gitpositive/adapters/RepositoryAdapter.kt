package org.acmvit.gitpositive.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.acmvit.gitpositive.R
import org.acmvit.gitpositive.models.Following
import org.acmvit.gitpositive.models.Repository

class RepositoryAdapter(private val dataSet: MutableList<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.name)
        val description : TextView = view.findViewById(R.id.description)
        val language : TextView = view.findViewById(R.id.language)
        val stars : TextView = view.findViewById(R.id.stars)
        val forks : TextView = view.findViewById(R.id.fork)
        val share : FloatingActionButton = view.findViewById(R.id.shareRepo)
        val clickMe : CardView = view.findViewById(R.id.clickMe)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.repo_layout, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet.elementAt(position).name
        viewHolder.description.text = dataSet.elementAt(position).description
        if(dataSet[position].stars==null){
            viewHolder.stars.text = "0"
        } else {
            viewHolder.stars.text = dataSet.elementAt(position).stars.toString()
        }
        if(dataSet[position].forks_count==null){
            viewHolder.forks.text = "0"
        } else {
            viewHolder.forks.text = dataSet.elementAt(position).forks_count.toString()
        }
        viewHolder.language.text = dataSet.elementAt(position).language

        viewHolder.share.setOnClickListener{
            val message = "Hey!! I found an amazing repository - '" + dataSet.elementAt(position).name +
                    "'. I would like to share it with you. " +
                    "Do give a star. " +
                    dataSet.elementAt(position).html_url
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            viewHolder.share.context.startActivity(Intent.createChooser(intent, "GitPositive"))
        }

        viewHolder.clickMe.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(dataSet.elementAt(position).html_url))
            viewHolder.clickMe.context.startActivity(browserIntent)
        }

    }
    override fun getItemCount() = dataSet.size

}
