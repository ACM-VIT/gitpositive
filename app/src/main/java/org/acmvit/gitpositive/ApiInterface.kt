package org.acmvit.gitpositive

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("users/{username}")
    fun getData(@Path("username") username:String? ): Call<UserData>
}
