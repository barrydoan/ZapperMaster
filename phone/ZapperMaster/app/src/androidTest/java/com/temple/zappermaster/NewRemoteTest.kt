package com.temple.zappermaster

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class NewRemoteTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickNewRemote() {
        // click on menu
        createRemote()
        // check have new remote on the list
        onView(withId(R.id.remoteListRecyclerView)).check(matches(hasDescendant(withText("Test model"))))
    }

    fun createRemote() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        // click on new remote menu
        onView(withText("New remote")).perform(click())
        // fill the information to the popup
        onView(withHint("Model Number")).perform(typeText("Test model"))
        onView(withHint("Type")).perform(typeText("TV"))
        onView(withHint("Manufacturer")).perform(typeText("Samsung"))
        // click confirm button
        onView(withText("CONFIRM")).perform(click())
        // click add a button
        onView(withId(R.id.btnAdd)).perform(click())
        // click save button
        onView(withId(R.id.btnSave)).perform(click())
    }


}