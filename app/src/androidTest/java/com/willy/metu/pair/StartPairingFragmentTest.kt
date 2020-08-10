package com.willy.metu.pair

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.willy.metu.MainActivity
import com.willy.metu.R
import org.junit.Rule
import org.junit.Test

class StartPairingFragmentTest {


    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)


    @Test
    fun clickStartToNavigate() {
        onView(withId(R.id.button_start))
                .perform(click())
    }
}