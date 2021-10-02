package org.acmvit.gitpositive.repositoryList.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.acmvit.gitpositive.ApiInterface
import org.acmvit.gitpositive.repositoryList.model.RepositoryResponseItem
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    val retrofit: ApiInterface
) : ViewModel() {

    private val _repoList: MutableState<List<RepositoryResponseItem>> = mutableStateOf(emptyList())
    val repoList: State<List<RepositoryResponseItem>> = _repoList

    fun getUserRepositories(username: String?) {
        viewModelScope.launch {
            try {
                val repoList = retrofit.getReposForUser(username = username)
                _repoList.value = repoList.toList()
            } catch (e: Exception) {
                Log.e("RepositoryViewModel", e.message.toString())
            }
        }
    }
}