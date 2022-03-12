package com.altop.githubuserapp.ui.detailUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altop.githubuserapp.api.ApiConfig
import com.altop.githubuserapp.data.model.DetailUserResponse
import com.altop.githubuserapp.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
  private val _user = MutableLiveData<DetailUserResponse>()
  val user: LiveData<DetailUserResponse> = _user
  
  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading
  
  private val _snackbarText = MutableLiveData<Event<String>>()
  val snackbarText: LiveData<Event<String>> = _snackbarText
  
  private val ERROR_MESSAGE = "Mohon maaf terjadi kesalahan"
  
  fun setUser(username: String) {
    _isLoading.postValue(true)
    val client = ApiConfig.getApiService().getUserDetail(username)
    
    client.enqueue(object : Callback<DetailUserResponse> {
      override fun onResponse(
        call: Call<DetailUserResponse>, response: Response<DetailUserResponse>
      ) {
        _isLoading.postValue(false)
        if (response.isSuccessful) {
          _user.postValue(response.body())
        } else {
          _snackbarText.value = Event(ERROR_MESSAGE)
        }
      }
      
      override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
        _isLoading.postValue(false)
        _snackbarText.value = Event(ERROR_MESSAGE)
      }
    })
  }
}