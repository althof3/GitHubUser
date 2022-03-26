package com.altop.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.altop.githubuserapp.R
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.databinding.ActivityMainBinding
import com.altop.githubuserapp.ui.detailUser.DetailUserActivity
import com.altop.githubuserapp.ui.favoriteUser.FavoriteUserActivity
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
  
  private lateinit var bind: ActivityMainBinding
  private val mainViewModel by viewModels<MainViewModel>()
  private var isNightMode by Delegates.notNull<Boolean>()
  private lateinit var sharedPrefsEdit: SharedPreferences.Editor
  private lateinit var toggleMenu: MenuItem
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityMainBinding.inflate(layoutInflater)
    setContentView(bind.root)
    supportActionBar?.title = TITLE
    
    
    
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
    
    val appSettingPrefs: SharedPreferences = getSharedPreferences("appSettingPrefs", MODE_PRIVATE)
    isNightMode = appSettingPrefs.getBoolean(NIGHT_MODE, false)
    sharedPrefsEdit = appSettingPrefs.edit()
    
    if (isNightMode) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    
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
          it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
          it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
          startActivity(it)
        }
      }
    })
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.option_menu, menu)
    toggleMenu = menu.getItem(1)
  
    if (isNightMode) {
      toggleMenu.icon = AppCompatResources.getDrawable(this, R.drawable.ic_nights)
    } else {
      toggleMenu.icon = AppCompatResources.getDrawable(this, R.drawable.ic_sunny)
    }
    return super.onCreateOptionsMenu(menu)
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.favorite_list -> {
        Intent(this, FavoriteUserActivity::class.java).also {
          startActivity(it)
        }
      }
      R.id.toggle_mode -> {
        Log.d("nightMode", isNightMode.toString())
        if (isNightMode) {
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
          sharedPrefsEdit.putBoolean(NIGHT_MODE, false)
        } else {
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
          sharedPrefsEdit.putBoolean(NIGHT_MODE, true)
        }
        sharedPrefsEdit.apply()
      }
    }
    return super.onOptionsItemSelected(item)
  }
  
  companion object {
    const val TITLE = "Github User\'s Search"
    const val NIGHT_MODE = "NIGHT_MODE"
  }
  
}