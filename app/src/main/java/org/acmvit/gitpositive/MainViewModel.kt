package org.acmvit.gitpositive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Rooparsh Kalia on 02/10/21
 */
class MainViewModel : ViewModel() {
    private val BaseURL = "https://api.github.com/"

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BaseURL)
        .build()
        .create(ApiInterface::class.java)


    fun getUserData(username: String) {
        _viewState.value = ViewState.Loading
        val retrofitData = retrofitBuilder.getData(username)
        retrofitData.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.isSuccessful && response.body() != null) {
                    _viewState.value = ViewState.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                _viewState.value = ViewState.Error(t.message.orEmpty())
            }
        })
    }


    sealed class ViewState {
        object Loading : ViewState()
        data class Error(val message: String) : ViewState()
        data class Success(val userData: UserData) : ViewState()
    }
}