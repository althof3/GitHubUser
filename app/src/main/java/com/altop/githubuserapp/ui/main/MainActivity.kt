package com.altop.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    bind.btnSearch.setOnClickListener {
      searchUser(it)
      
    }
    
  }
  
  private fun searchUser(v: View) {
    bind.apply {
      val query = etQuery.text.toString()
      if (query.isEmpty()) return
      
      mainViewModel.findUser(query)
      etQuery.clearFocus()
      
      val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
  }
  
  private fun setUpSearchBar() {
    bind.etQuery.setOnKeyListener { view, code, event ->
      if (event.action == KeyEvent.ACTION_DOWN && code == KeyEvent.KEYCODE_ENTER) {
        searchUser(view)
        
        return@setOnKeyListener true
      }
      return@setOnKeyListener false
    }
  }
  
  private fun showLoading(isLoading: Boolean) {
    bind.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
  
  private fun showRecyclerList(users: List<GithubUser>) {
    bind.rvUsers.layoutManager = LinearLayoutManager(this)
    val listUserAdapter = ListUserAdapter(users)
    bind.rvUsers.adapter = listUserAdapter
    
    if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      bind.rvUsers.layoutManager = GridLayoutManager(this, 2)
    } else {
      bind.rvUsers.layoutManager = LinearLayoutManager(this)
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