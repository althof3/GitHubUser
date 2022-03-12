package com.altop.githubuserapp.ui.detailUser.followers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altop.githubuserapp.api.ApiConfig
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
  private val _listFollowers = MutableLiveData<List<GithubUser>>()
  val listFollowers: LiveData<List<GithubUser>> = _listFollowers
  
  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading
  
  private val _snackbarText = MutableLiveData<Event<String>>()
  val snackbarText: LiveData<Event<String>> = _snackbarText
  
  private val ERROR_MESSAGE = "Mohon maaf terjadi kesalahan"
  
  fun setListFollowers(username: String) {
    val client = ApiConfig.getApiService().getUserFollowers(username)
    _isLoading.postValue(true)
    client.enqueue(object : Callback<List<GithubUser>> {
      override fun onResponse(
        call: Call<List<GithubUser>>, response: Response<List<GithubUser>>
      ) {
        _isLoading.postValue(false)
        if (response.isSuccessful) {
          _listFollowers.postValue(response.body())
        } else {
          _snackbarText.value = Event(ERROR_MESSAGE)
        }
      }
      
      override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
        _isLoading.postValue(false)
        _snackbarText.value = Event(ERROR_MESSAGE)
      }
    })
  }
}