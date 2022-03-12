package com.altop.githubuserapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altop.githubuserapp.api.ApiConfig
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.data.model.SearchResponse
import com.altop.githubuserapp.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
  private val _users = MutableLiveData<List<GithubUser>>()
  val users: LiveData<List<GithubUser>> = _users
  
  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading
  
  private val _snackbarText = MutableLiveData<Event<String>>()
  val snackbarText: LiveData<Event<String>> = _snackbarText
  
  private val ERROR_MESSAGE = "Mohon maaf terjadi kesalahan"
  
  init {
    findUser("a")
  }
  
  fun findUser(query: String) {
    _isLoading.postValue(true)
    val client = ApiConfig.getApiService().getSearchUsers(query)
    
    client.enqueue(object : Callback<SearchResponse> {
      override fun onResponse(
        call: Call<SearchResponse>, response: Response<SearchResponse>
      ) {
        _isLoading.postValue(false)
        if (response.isSuccessful) {
          _users.postValue(response.body()?.items)
        } else {
          _snackbarText.value = Event(ERROR_MESSAGE)
        }
      }
      
      override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
        _isLoading.postValue(false)
        _snackbarText.value = Event(ERROR_MESSAGE)
      }
    })
  }
  
}