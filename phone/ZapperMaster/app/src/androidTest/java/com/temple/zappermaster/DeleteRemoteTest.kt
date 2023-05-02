package com.temple.zappermaster

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class DeleteRemoteTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun createRemote() {
        NewRemoteTest().createRemote()
    }


    @Test
    fun testDeleteRemote() {
        // count element before test
        var beforeTestCount = getCountFromRecyclerView(R.id.remoteListRecyclerView)
        // click
        onView(withId(R.id.remoteListRecyclerView)).perform(RecyclerViewActions.actionOnItem<RemoteListFragment.RemoteAdapter.RemoteViewHolder>(hasDescendant(withText("Test model")), clickOnViewChild(R.id.btnDelete)))
        // count element after test
        var afterTestCount = getCountFromRecyclerView(R.id.remoteListRecyclerView)
        Assert.assertEquals(beforeTestCount, afterTestCount + 1)
    }

    fun getCountFromRecyclerView(@IdRes RecyclerViewId: Int): Int {
        val count = intArrayOf(0)
        val matcher: Matcher<View> = object : TypeSafeMatcher<View>() {

            override fun describeTo(description: Description) {}
            override fun matchesSafely(item: View?): Boolean {
                count[0] = (item as RecyclerView).adapter!!.itemCount
                return true
            }
        }
        onView(allOf(withId(RecyclerViewId), isDisplayed())).check(matches(matcher))
        return count[0]
    }


    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }

}