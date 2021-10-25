package org.acmvit.gitpositive.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.acmvit.gitpositive.datasource.RepositorySource
import org.acmvit.gitpositive.remote.ApiInterface
import org.acmvit.gitpositive.remote.model.RepositoryResponseItem
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    val retrofit: ApiInterface
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 10
    }

    fun reposFlow(userName: String): Flow<PagingData<RepositoryResponseItem>> = Pager(PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE)) {
        RepositorySource(userName, retrofit)
    }.flow.cachedIn(viewModelScope)

}