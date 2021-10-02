package org.acmvit.gitpositive

import org.acmvit.gitpositive.repositoryList.model.RepositoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("users/{username}")
    fun getData(@Path("username") username: String?): Call<UserData>

    @GET("users/{username}/repos")
    suspend fun getReposForUser(@Path("username") username: String?): RepositoryResponse
}
