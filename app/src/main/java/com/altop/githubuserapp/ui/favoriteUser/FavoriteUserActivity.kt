package com.altop.githubuserapp.ui.favoriteUser

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.altop.githubuserapp.data.local.FavoriteUser
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.altop.githubuserapp.ui.detailUser.DetailUserActivity
import com.altop.githubuserapp.ui.main.ListUserAdapter

class FavoriteUserActivity : AppCompatActivity() {
  
  private val FavoriteViewModel by viewModels<FavoriteViewModel>()
  private lateinit var bind: ActivityFavoriteUserBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityFavoriteUserBinding.inflate(layoutInflater)
    
    setContentView(bind.root)
    
    bind.rvUsers.setHasFixedSize(true)
    
    FavoriteViewModel.getFavoriteUser()?.observe(this) {
      if (it != null) showRecyclerList(it)
      
    }
  }

//  private fun mapList
  
  private fun showRecyclerList(users: List<FavoriteUser>) {
    bind.rvUsers.layoutManager = LinearLayoutManager(this)
    val userList = mapList(users)
    val listUserAdapter = ListUserAdapter(userList)
    bind.rvUsers.adapter = listUserAdapter
    
    bind.rvUsers.layoutManager =
      if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        GridLayoutManager(this, 2)
      } else {
        LinearLayoutManager(this)
      }
    
    listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
      override fun onItemClicked(data: GithubUser) {
        Intent(this@FavoriteUserActivity, DetailUserActivity::class.java).also {
          it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
          it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
          it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
          startActivity(it)
        }
      }
    })
  }
  
  private fun mapList(users: List<FavoriteUser>): List<GithubUser> {
    val userList = ArrayList<GithubUser>()
    
    for (user in users) {
      user.apply {
        userList.add(GithubUser(login, id, avatar_url))
      }
    }
    return userList.toList()
  }
}