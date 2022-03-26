package com.altop.githubuserapp.ui.detailUser

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.altop.githubuserapp.R
import com.altop.githubuserapp.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
  
  private lateinit var bind: ActivityDetailUserBinding
  private val detailViewModel by viewModels<DetailUserViewModel>()
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityDetailUserBinding.inflate(layoutInflater)
    setContentView(bind.root)
    supportActionBar?.title = TITLE
    
    val bundle = Bundle()
    
    val username = intent.getStringExtra(EXTRA_USERNAME) as String
    val avatarUrl = intent.getStringExtra(EXTRA_AVATAR) as String
    val id = intent.getIntExtra(EXTRA_ID, 0)
    
    
    detailViewModel.setUser(username)
    detailViewModel.user.observe(this) {
      if (it != null) {
        bind.apply {
          Glide.with(this@DetailUserActivity).load(it.avatarUrl).circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade()).into(avatar)
          tvUsername.text = resources.getString(R.string.template_id, it.login.toString())
          tvName.text = it.name
          tvCompany.text = it.company ?: "Company"
          tvLocation.text = it.location ?: "Location"
          tvRepository.text = it.repos.toString()
          tvFollower.text = it.followers.toString()
          tvFollowing.text = it.following.toString()
        }
      }
    }
    bundle.putString(EXTRA_USERNAME, username)
    
    detailViewModel.isLoading.observe(this) {
      showLoading(it)
    }
    
    detailViewModel.snackbarText.observe(this) {
      it.getContentIfNotHandled()?.let { snackBarText ->
        Snackbar.make(
          window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT
        ).show()
      }
    }
    
    var _isChecked = false
    CoroutineScope(Dispatchers.IO).launch {
      val count = detailViewModel.checkUser(id)
      withContext(Dispatchers.Main) {
        if (count != null) {
          if (count > 0) {
            bind.toggleFavorite.isChecked = true
            _isChecked = true
          } else {
            bind.toggleFavorite.isChecked = false
            _isChecked = false
          }
          
        }
      }
    }
    
    bind.toggleFavorite.setOnClickListener {
      _isChecked = !_isChecked
      
      if (_isChecked) detailViewModel.addToFavorite(username, id, avatarUrl)
      else detailViewModel.removeFavoriteUser(id)
      
      bind.toggleFavorite.isChecked = _isChecked
    }
    
    bind.apply {
      
      viewPager.adapter = SectionsPagerAdapter(this@DetailUserActivity, bundle)
      TabLayoutMediator(tabs, viewPager) { tab, position ->
        tab.text = resources.getString(TAB_TITLES[position])
      }.attach()
      supportActionBar?.elevation = 0f
    }
    
  }
  
  private fun showLoading(isLoading: Boolean) {
    bind.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
  
  companion object {
    const val EXTRA_USERNAME = "EXTRA_USERNAME"
    const val EXTRA_ID = "EXTRA_ID"
    const val EXTRA_AVATAR = "EXTRA_AVATAR"
    
    const val TITLE = "Detail User"
    
    @StringRes
    private val TAB_TITLES = intArrayOf(
      R.string.following, R.string.follower
    )
  }
  
}
