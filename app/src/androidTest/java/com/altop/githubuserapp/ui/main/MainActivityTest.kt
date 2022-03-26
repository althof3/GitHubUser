package com.altop.githubuserapp.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.altop.githubuserapp.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
  
  @Before
  fun setup() {
    ActivityScenario.launch(MainActivity::class.java)
  }
  
  @Test
  fun checkActiviyVisibility() {
    onView(withId(R.id.main_activity)).check(matches(isDisplayed()))
    onView(withId(R.id.progres_bar)).check(matches(isDisplayed()))
    onView(withId(R.id.et_query)).check(matches(isDisplayed()))
  }
}