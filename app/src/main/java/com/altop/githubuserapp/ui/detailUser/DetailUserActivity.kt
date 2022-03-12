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

class DetailUserActivity : AppCompatActivity() {
  
  private lateinit var bind: ActivityDetailUserBinding
  private val detailViewModel by viewModels<DetailUserViewModel>()
  
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityDetailUserBinding.inflate(layoutInflater)
    setContentView(bind.root)
    supportActionBar?.title = "Detail User"
    
    val bundle = Bundle()
    
    val username = intent.getStringExtra(EXTRA_USERNAME) as String
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
          tvRepository.text =
            resources.getString(R.string.template_repositories, it.repos.toString())
          tvFollower.text =
            resources.getString(R.string.template_followers, it.followers.toString())
          tvFollowing.text =
            resources.getString(R.string.template_following, it.following.toString())
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
    
    @StringRes
    private val TAB_TITLES = intArrayOf(
      R.string.following, R.string.follower
    )
  }
  
}
