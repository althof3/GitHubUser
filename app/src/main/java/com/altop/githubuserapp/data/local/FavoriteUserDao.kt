package com.altop.githubuserapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
  
  @Insert
  suspend fun addToFavorite(user: FavoriteUser)
  
  @Query("SELECT * FROM favorite_user")
  fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

  @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
  fun checkUser(id: Int): Int
  
  @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
  suspend fun removeFavoriteUser(id: Int): Int
  
}