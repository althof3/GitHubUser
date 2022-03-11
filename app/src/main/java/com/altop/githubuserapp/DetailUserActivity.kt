package com.altop.githubuserapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.altop.githubuserapp.databinding.ActivityDetailUserBinding
import com.altop.githubuserapp.model.User
import com.bumptech.glide.Glide

class DetailUserActivity : AppCompatActivity() {
  private lateinit var bind: ActivityDetailUserBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityDetailUserBinding.inflate(layoutInflater)
    setContentView(bind.root)
    
    val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
    
    Glide.with(this).load(user.avatar).circleCrop().into(bind.avatar)
    
    bind.itemName.text = user.name
    bind.itemUsername.text = String.format(resources.getString(R.string.template_id), user.username)
    bind.itemCompany.text = user.company
    bind.itemLocation.text = user.location
    bind.userRepository.text =
      String.format(resources.getString(R.string.template_repositories), user.repository)
    bind.userFollower.text =
      String.format(resources.getString(R.string.template_followers), user.followers)
    bind.userFollowing.text =
      String.format(resources.getString(R.string.template_following), user.following)
  }
  
  companion object {
    const val EXTRA_USER = "EXTRA_USER"
  }
}
