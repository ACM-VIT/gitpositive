package org.acmvit.gitpositive.ui.repository

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.acmvit.gitpositive.remote.ApiInterface
import org.acmvit.gitpositive.remote.model.RepositoryResponseItem
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    val retrofit: ApiInterface
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 30
    }

    private val _repoList: MutableState<List<RepositoryResponseItem>> = mutableStateOf(emptyList())
    val repoList: State<List<RepositoryResponseItem>> = _repoList

    // Pagination starts at '1' (-1 = exhausted)
    val page = mutableStateOf(1)

    var repoListScrollPosition = 0

    val loading = mutableStateOf(false)

    val loadingMoreItem = mutableStateOf(false)

    fun getUserRepositories(username: String?) {
        viewModelScope.launch {
            loading.value = true
            resetSearchState()
//            delay(2000)
            try {
                val repoList = retrofit.getReposForUser(username = username, page = page.value)
                _repoList.value = repoList.toList()

            } catch (e: Exception) {
                Log.e("RepositoryViewModel", e.message.toString())
            }
            loading.value = false
        }
    }

    fun nextPage(username:String?){
        viewModelScope.launch {
            // prevent duplicate event due to recompose happening to quickly
            if((repoListScrollPosition + 1) >= (page.value * PAGE_SIZE) ){
                loading.value = true
                loadingMoreItem.value = true
                incrementPage()
                Log.d("RepositoryViewModel", "nextPage: triggered: ${page.value}")
//                delay(1000)
                if(page.value > 1){
                    val result = retrofit.getReposForUser(username = username, page = page.value)
                    Log.d("RepositoryViewModel", "search: appending")
                    appendRepos(result)
                }
                loading.value = false
                loadingMoreItem.value = false
            }
        }
    }


    // Append new repos to existing repo list
    private fun appendRepos(repos: List<RepositoryResponseItem>){
        val current = ArrayList(this._repoList.value)
        current.addAll(repos)
        this._repoList.value = current
    }

    private fun incrementPage(){
        page.value = page.value + 1
    }

    fun onChangeRepoScrollPosition(position: Int){
        repoListScrollPosition = position
    }

    /**
     * Called when getUserRepositories is executed.
     */
    private fun resetSearchState() {
        _repoList.value = listOf()
        page.value = 1
        onChangeRepoScrollPosition(0)
    }
}