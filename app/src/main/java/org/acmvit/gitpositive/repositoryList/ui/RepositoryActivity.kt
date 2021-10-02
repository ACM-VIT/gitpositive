package org.acmvit.gitpositive.repositoryList.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import org.acmvit.gitpositive.repositoryList.model.RepositoryResponseItem
import org.acmvit.gitpositive.repositoryList.viewmodel.RepositoryViewModel

class RepositoryActivity : AppCompatActivity() {

    private lateinit var viewModel: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RepositoryViewModel::class.java)
        val username = intent.getStringExtra("Username")
        viewModel.getUserRepositories(username)
        setContent {
            RepositoryListScreen(viewModel)
        }
    }
}

@Composable
fun RepositoryListScreen(viewModel: RepositoryViewModel) {
    val list by remember {
        viewModel.repoList
    }
    Surface(color = Color(0xFF1C1C1C)) {
        LazyColumn {
            items(list) { item ->
                SingleRepoItem(item)
            }
        }
    }
}

@Composable
fun SingleRepoItem(
    item: RepositoryResponseItem
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color(0xFF313131),
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
                    fontSize = 20.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description ?: "",
                style = TextStyle(fontSize = 14.sp, color = Color.White)
            )
        }
    }
}
