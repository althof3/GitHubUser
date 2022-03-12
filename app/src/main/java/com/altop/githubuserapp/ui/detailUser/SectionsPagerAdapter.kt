package com.altop.githubuserapp.ui.detailUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.altop.githubuserapp.ui.detailUser.followers.FollowersFragment
import com.altop.githubuserapp.ui.detailUser.following.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, data: Bundle) :
  FragmentStateAdapter(activity) {
  
  private var fragmentBundle: Bundle
  
  init {
    fragmentBundle = data
  }
  
  override fun getItemCount(): Int {
    return 2
  }
  
  override fun createFragment(position: Int): Fragment {
    var fragment: Fragment? = null
    when (position) {
      0 -> fragment = FollowingFragment()
      1 -> fragment = FollowersFragment()
    }
    fragment?.arguments = this.fragmentBundle
    return fragment as Fragment
  }
  
}