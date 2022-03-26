package com.altop.githubuserapp.ui.detailUser.following

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.altop.githubuserapp.R
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.databinding.FragmentFollowBinding
import com.altop.githubuserapp.ui.detailUser.DetailUserActivity
import com.altop.githubuserapp.ui.main.ListUserAdapter
import com.google.android.material.snackbar.Snackbar

class FollowingFragment : Fragment(R.layout.fragment_follow) {
  
  private var _binding: FragmentFollowBinding? = null
  private val binding get() = _binding!!
  private val followingViewModel by viewModels<FollowingViewModel>()
  private lateinit var adapter: ListUserAdapter
  private lateinit var username: String
  
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFollowBinding.inflate(inflater, container, false)
    username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
    
    followingViewModel.apply {
      setUserFollowing(username)
      listFollowing.observe(viewLifecycleOwner) {
        showFollowersList(it)
      }
      
      isLoading.observe(viewLifecycleOwner) {
        showLoading(it)
      }
      snackbarText.observe(viewLifecycleOwner) {
        it.getContentIfNotHandled()?.let { snackBarText ->
          Snackbar.make(
            requireActivity().window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT
          ).show()
        }
      }
    }
    
    return binding.root
  }
  
  private fun showFollowersList(users: List<GithubUser>) {
    binding.rvUsers.layoutManager = LinearLayoutManager(activity)
    adapter = ListUserAdapter(users)
    binding.rvUsers.adapter = adapter
    
    if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      binding.rvUsers.layoutManager = GridLayoutManager(activity, 2)
    } else {
      binding.rvUsers.layoutManager = LinearLayoutManager(activity)
    }
    
    adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
      override fun onItemClicked(data: GithubUser) {
        Intent(activity, DetailUserActivity::class.java).also {
          it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
          it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
          it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
          startActivity(it)
        }
      }
    })
  }
  
  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
  
  private fun showLoading(isLoading: Boolean) {
    binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
}