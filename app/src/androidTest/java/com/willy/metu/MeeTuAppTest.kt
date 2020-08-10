package com.willy.metu


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.willy.metu.login.LoginActivity
import com.willy.metu.login.UserManager
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MeeTuAppTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginActivityTest() {
        val appCompatButton = onView(
                allOf(withId(R.id.google_sign_in_button), withText("Sign Up With Google"),
                        childAtPosition(
                                allOf(withId(R.id.button_layout),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                0),
                        isDisplayed()))
        Thread.sleep(2000)
        if (UserManager.user.email == "") {
            appCompatButton.perform(click())
        }


    }

    @Test
    fun checkBottomNavigation() {
        val bottomNavigationTalentPool = onView(
                allOf(withId(R.id.navigation_talent_pool), withContentDescription("Articles"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavView),
                                        0),
                                1),
                        isDisplayed()))

        val bottomNavigationPair = onView(
                allOf(withId(R.id.navigation_pairing), withContentDescription("Discover"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavView),
                                        0),
                                2),
                        isDisplayed()))

        val bottomNavigationMessage = onView(
                allOf(withId(R.id.navigation_chat_list), withContentDescription("Message"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavView),
                                        0),
                                3),
                        isDisplayed()))

        val bottomNavigationNotify = onView(
                allOf(withId(R.id.navigation_notification), withContentDescription("Notification"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavView),
                                        0),
                                4),
                        isDisplayed()))

        val bottomNavigationHome = onView(
                allOf(withId(R.id.navigation_home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavView),
                                        0),
                                0),
                        isDisplayed()))

        bottomNavigationTalentPool.perform(click())
        Thread.sleep(2000)
        bottomNavigationPair.perform(click())
        Thread.sleep(2000)
        bottomNavigationMessage.perform(click())
        Thread.sleep(2000)
        bottomNavigationNotify.perform(click())
        Thread.sleep(2000)
        bottomNavigationHome.perform(click())

    }

    @Test
    fun testDrawer() {

        val toggleDrawer = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))

        val toggleDrawer2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()))


        val navigationMenuItemProfile = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.drawerNavView),
                                        0)),
                        1),
                        isDisplayed()))

        val toggleBack = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))

        val toggleBack2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()))

        val navigationMenuItemFollow = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.drawerNavView),
                                        0)),
                        2),
                        isDisplayed()))

        Thread.sleep(5000)
        toggleDrawer.perform(click())
        Thread.sleep(2000)

        navigationMenuItemProfile.perform(click())
        Thread.sleep(2000)

        toggleBack.perform(click())
        Thread.sleep(4000)

        toggleDrawer2.perform(click())
        Thread.sleep(2000)

        navigationMenuItemFollow.perform(click())
        Thread.sleep(2000)

        toggleBack2.perform(click())

    }

    @Test
    fun testCalendarSwipeChange() {

        val actionMenuItemView = onView(
                allOf(withId(R.id.item_calendar),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                0),
                        isDisplayed()))


        val monthView = onView(
                allOf(childAtPosition(
                        allOf(withContentDescription("Calendar"),
                                childAtPosition(
                                        withId(R.id.mcv_pager),
                                        0)),
                        17), isDisplayed()))

        val fullView = onView(
                allOf(withId(R.id.view_container))
        )


        val calendarView = onView(
                allOf(childAtPosition(
                        allOf(withContentDescription("Calendar"),
                                childAtPosition(
                                        withId(R.id.mcv_pager),
                                        0)),
                        7), isDisplayed()))


        Thread.sleep(7000)
        actionMenuItemView.perform(click())

        Thread.sleep(2000)
        monthView.check(matches(isDisplayed()))

        Thread.sleep(4000)
        fullView.perform(swipeUp())

        Thread.sleep(4000)
        calendarView.check(matches(isDisplayed()))


    }

}


private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }
}

