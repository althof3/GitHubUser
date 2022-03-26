package com.altop.githubuserapp.ui.favoriteUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.altop.githubuserapp.data.local.FavoriteUser
import com.altop.githubuserapp.data.local.FavoriteUserDao
import com.altop.githubuserapp.data.local.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
  private var userDao: FavoriteUserDao?
  private var userDb: UserDatabase?
  
  init {
    userDb = UserDatabase.getDatabase(application)
    userDao = userDb?.favoriteUserDao()
  }
  
  fun getFavoriteUser(): LiveData<List<FavoriteUser>>? = userDao?.getFavoriteUsers()
}