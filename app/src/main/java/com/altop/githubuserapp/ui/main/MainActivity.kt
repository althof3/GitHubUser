package com.altop.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.databinding.ActivityMainBinding
import com.altop.githubuserapp.ui.detailUser.DetailUserActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  
  private lateinit var bind: ActivityMainBinding
  private val mainViewModel by viewModels<MainViewModel>()
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityMainBinding.inflate(layoutInflater)
    setContentView(bind.root)
    supportActionBar?.title = "Github User's Search"
    
    bind.rvUsers.setHasFixedSize(true)
    
    mainViewModel.users.observe(this) {
      showRecyclerList(it)
    }
    mainViewModel.isLoading.observe(this) {
      showLoading(it)
    }
    mainViewModel.snackbarText.observe(this) {
      it.getContentIfNotHandled()?.let { snackBarText ->
        Snackbar.make(
          window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT
        ).show()
      }
    }
    
    setUpSearchBar()
    
  }
  
  private fun searchUser(query: String) {
    if (query.isEmpty()) return
    
    mainViewModel.findUser(query)
    
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(bind.root.windowToken, 0)
  }
  
  private fun setUpSearchBar() {
    bind.etQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      
      override fun onQueryTextSubmit(query: String): Boolean {
        searchUser(query)
        return true
      }
      
      override fun onQueryTextChange(p0: String?): Boolean {
        return false
      }
    })
  }
  
  private fun showLoading(isLoading: Boolean) {
    bind.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
  
  private fun showRecyclerList(users: List<GithubUser>) {
    bind.rvUsers.layoutManager = LinearLayoutManager(this)
    val listUserAdapter = ListUserAdapter(users)
    bind.rvUsers.adapter = listUserAdapter
    
    bind.rvUsers.layoutManager =
      if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        GridLayoutManager(this, 2)
      } else {
        LinearLayoutManager(this)
      }
    
    listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
      override fun onItemClicked(data: GithubUser) {
        Intent(this@MainActivity, DetailUserActivity::class.java).also {
          it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
          startActivity(it)
        }
      }
    })
  }
  
}