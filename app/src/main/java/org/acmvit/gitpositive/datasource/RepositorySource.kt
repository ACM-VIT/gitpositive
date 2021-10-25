package org.acmvit.gitpositive.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.acmvit.gitpositive.remote.ApiInterface
import org.acmvit.gitpositive.remote.model.RepositoryResponseItem
import retrofit2.HttpException
import java.io.IOException

class RepositorySource(
    private val userName: String,
    private val retrofit: ApiInterface
): PagingSource<Int, RepositoryResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryResponseItem> {
        return try {
            val nextPage = params.key ?: 1
            val userList = retrofit.getReposForUser(username = userName, page = nextPage, perPage = params.loadSize)
            LoadResult.Page(
                data = userList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (userList.isEmpty()) null else nextPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryResponseItem>): Int? {
        return state.anchorPosition
    }
}