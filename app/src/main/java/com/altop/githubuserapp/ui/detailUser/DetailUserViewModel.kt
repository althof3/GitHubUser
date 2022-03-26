package com.altop.githubuserapp.ui.detailUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altop.githubuserapp.api.ApiConfig
import com.altop.githubuserapp.data.local.FavoriteUser
import com.altop.githubuserapp.data.local.FavoriteUserDao
import com.altop.githubuserapp.data.local.UserDatabase
import com.altop.githubuserapp.data.model.DetailUserResponse
import com.altop.githubuserapp.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
  private val _user = MutableLiveData<DetailUserResponse>()
  val user: LiveData<DetailUserResponse> = _user
  
  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading
  
  private val _snackbarText = MutableLiveData<Event<String>>()
  val snackbarText: LiveData<Event<String>> = _snackbarText
  
  private val ERROR_MESSAGE = "Mohon maaf terjadi kesalahan"
  
  private var userDao: FavoriteUserDao?
  private var userDb: UserDatabase?
  
  init {
    userDb = UserDatabase.getDatabase(application)
    userDao = userDb?.favoriteUserDao()
  }
  
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
  
  fun addToFavorite(username: String, id: Int, avatarUrl: String) {
    CoroutineScope(Dispatchers.IO).launch {
      val user = FavoriteUser(login = username, id = id, avatar_url = avatarUrl)
      userDao?.addToFavorite(user)
    }
  }
  
  fun checkUser(id: Int) = userDao?.checkUser(id)
  
  fun removeFavoriteUser(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
      userDao?.removeFavoriteUser(id)
    }
  }
}