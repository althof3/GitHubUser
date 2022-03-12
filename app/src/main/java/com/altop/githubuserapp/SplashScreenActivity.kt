package com.altop.githubuserapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.altop.githubuserapp.databinding.ActivitySplashScreenBinding
import com.altop.githubuserapp.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {
  private lateinit var bind: ActivitySplashScreenBinding
  private val DURATION = 1500L
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivitySplashScreenBinding.inflate(layoutInflater)
    setContentView(bind.root)
    
    bind.codeLogo.alpha = 0f
    
    bind.codeLogo.animate().setDuration(DURATION).alpha(1f).withEndAction {
      val i = Intent(this, MainActivity::class.java)
      startActivity(i)
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
      
      finish()
    }
  }
}