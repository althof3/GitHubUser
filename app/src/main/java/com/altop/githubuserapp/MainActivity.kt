package com.altop.githubuserapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.altop.githubuserapp.adapter.ListUserAdapter
import com.altop.githubuserapp.databinding.ActivityMainBinding
import com.altop.githubuserapp.model.User

class MainActivity : AppCompatActivity() {
  private val users = ArrayList<User>()
  private lateinit var bind: ActivityMainBinding
  
  private val listUsers: ArrayList<User>
    get() {
      val dataUserName = resources.getStringArray(R.array.username)
      val dataName = resources.getStringArray(R.array.name)
      val dataLocation = resources.getStringArray(R.array.location)
      val dataRepo = resources.getStringArray(R.array.repository)
      val dataCompany = resources.getStringArray(R.array.company)
      val dataFollowers = resources.getStringArray(R.array.followers)
      val dataFollowing = resources.getStringArray(R.array.following)
      val dataAvatar = resources.obtainTypedArray(R.array.avatar)
      
      val listUser = ArrayList<User>()
      for (i in dataName.indices) {
        val hero = User(
          name = dataName[i],
          username = dataUserName[i],
          avatar = dataAvatar.getResourceId(i, -1),
          location = dataLocation[i],
          repository = dataRepo[i],
          company = dataCompany[i],
          followers = dataFollowers[i],
          following = dataFollowing[i]
        )
        listUser.add(hero)
      }
      dataAvatar.recycle()
      return listUser
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityMainBinding.inflate(layoutInflater)
    setContentView(bind.root)
    
    bind.rvUsers.setHasFixedSize(true)
    users.addAll(listUsers)
    showRecyclerList()
  }
  
  private fun showRecyclerList() {
    bind.rvUsers.layoutManager = LinearLayoutManager(this)
    val listUserAdapter = ListUserAdapter(users)
    bind.rvUsers.adapter = listUserAdapter
    if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      bind.rvUsers.layoutManager = GridLayoutManager(this, 2)
    } else {
      bind.rvUsers.layoutManager = LinearLayoutManager(this)
    }
    
    listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
      override fun onItemClicked(data: User) {
        val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
        intentToDetail.putExtra(DetailUserActivity.EXTRA_USER, data)
        startActivity(intentToDetail)
      }
    })
  }
}