package com.temple.zappermaster

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class UseRemoteTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun createRemote() {
        NewRemoteTest().createRemote()
    }

    @Test
    fun testClickRemote() {
        // click on remote
        Espresso.onView(ViewMatchers.withText("Test model")).perform(ViewActions.click())
        // check if it have a button and click
        Espresso.onView(ViewMatchers.withText("NEW BUTTON")).perform(ViewActions.click())

    }
}