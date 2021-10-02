package org.acmvit.gitpositive.repositoryList.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.acmvit.gitpositive.ApiInterface
import org.acmvit.gitpositive.BaseURL
import org.acmvit.gitpositive.repositoryList.model.RepositoryResponseItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryViewModel : ViewModel() {

    private var retrofit: ApiInterface = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BaseURL)
        .build()
        .create(ApiInterface::class.java)

    private val _repoList: MutableState<List<RepositoryResponseItem>> = mutableStateOf(emptyList())
    val repoList: State<List<RepositoryResponseItem>> = _repoList

    fun getUserRepositories(username: String?) {
        viewModelScope.launch {
            val a = retrofit.getReposForUser(username = username)
            _repoList.value = a.toList()
            Log.d("BK", a.toString())
        }
    }
}