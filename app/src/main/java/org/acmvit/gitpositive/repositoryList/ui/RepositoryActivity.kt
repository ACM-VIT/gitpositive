package org.acmvit.gitpositive.repositoryList.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.acmvit.gitpositive.activities.AboutActivity
import org.acmvit.gitpositive.MainActivity
import org.acmvit.gitpositive.repositoryList.model.RepositoryResponseItem
import org.acmvit.gitpositive.repositoryList.viewmodel.RepositoryViewModel


@AndroidEntryPoint
class RepositoryActivity : AppCompatActivity() {

    private lateinit var viewModel: RepositoryViewModel
    var vibrator: Vibrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RepositoryViewModel::class.java)
        val username = intent.getStringExtra("Username")
        viewModel.getUserRepositories(username)

        setContent {
            Column() {
                pageTitle()
                RepositoryListScreen(viewModel) {
                    loadRepoInWeb(it)
                }
            }

        }
    }
    private fun loadRepoInWeb(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
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
    fun back(){
        onBackPressed()
    }
    fun info(){
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
    @Composable
    fun RepositoryListScreen(viewModel: RepositoryViewModel, onItemClick: (String) -> Unit) {
        val list by remember {
            viewModel.repoList
        }
        Surface(color = if (isSystemInDarkTheme()) Color(0xFF212121) else Color(0xffffffff)) {
            LazyColumn {
                items(list) { item ->
                    SingleRepoItem(item) { onItemClick(it) }
                }
            }
        }
    }
    @Composable
    fun pageTitle() {
        Surface(color = if (isSystemInDarkTheme()) Color(0xFF212121) else Color(0xffffffff)) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
                    .background(if (isSystemInDarkTheme()) Color(0xFF212121) else Color(0xffffffff)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(onClick = { back() },
                        backgroundColor = if (isSystemInDarkTheme()) Color(0xFF313131) else Color(0xFFE6E6E6),
                        contentColor = if (isSystemInDarkTheme()) Color(0xFFE6E6E6) else Color(0xFF1C1C1C),
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)

                    ) {
                        Icon(Icons.Filled.ArrowBack, "")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Row(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentHeight(),
                            text = "Your ",
                            style = TextStyle(
                                color = Color(0xFF6CFF54),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight(),
                            text = "Repositories",
                            style = TextStyle(
                                color = if (isSystemInDarkTheme()) Color(0xFFE6E6E6) else Color(0xFF1C1C1C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FloatingActionButton(onClick = { info() },
                        backgroundColor = if (isSystemInDarkTheme()) Color(0xFF313131) else Color(0xFFE6E6E6),
                        contentColor = if (isSystemInDarkTheme()) Color(0xFFE6E6E6) else Color(0xFF1C1C1C),
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)

                    ) {
                        Icon(Icons.Filled.Info, "")
                    }

                }
            }
        }

    }
    @Composable
    fun SingleRepoItem(
        item: RepositoryResponseItem,
        onClick: (String) -> Unit
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .background(if (isSystemInDarkTheme()) Color(0xFF212121) else Color(0xffffffff))
        ) {

            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        onClick(item.html_url)
                    },
                shape = RoundedCornerShape(12.dp),
                contentColor = if (isSystemInDarkTheme()) Color(0xFFE6E6E6) else Color(0xFF1C1C1C),
                backgroundColor = if (isSystemInDarkTheme()) Color(0xFF313131) else Color(0xFFE6E6E6)
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
                        style = TextStyle(fontSize = 14.sp),
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }

}



