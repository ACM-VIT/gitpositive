package org.acmvit.gitpositive.repositoryList.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.repositoryList.model.RepositoryResponseItem
import org.acmvit.gitpositive.repositoryList.viewmodel.RepositoryViewModel
import android.content.Intent
import android.net.Uri


@AndroidEntryPoint
class RepositoryActivity : AppCompatActivity() {

    private lateinit var viewModel: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RepositoryViewModel::class.java)
        val username = intent.getStringExtra("Username")
        viewModel.getUserRepositories(username)
        setContent {
            RepositoryListScreen(viewModel) {
                loadRepoInWeb(it)
            }
        }
    }

    private fun loadRepoInWeb(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}

@Composable
fun RepositoryListScreen(viewModel: RepositoryViewModel, onItemClick: (String) -> Unit) {
    val list by remember {
        viewModel.repoList
    }
    Surface {
        LazyColumn {
            items(list) { item ->
                SingleRepoItem(item) { onItemClick(it) }
            }
        }
    }
}

@Composable
fun SingleRepoItem(
    item: RepositoryResponseItem,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .fillMaxWidth()
            .clickable {
                onClick(item.html_url)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.language ?: "",
                style = TextStyle(
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description ?: "",
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}
