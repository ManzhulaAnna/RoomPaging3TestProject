package com.test.test

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.test.R
import com.test.api.MockApi
import com.test.util.TestUtils.withRecyclerView
import com.test.ui.MainActivity
import com.test.ui.fragments.events.adapter.EventsViewHolder
import com.test.ui.fragments.favorites.adapter.FavoritesViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class EventsListInstrumentedTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Inject
    lateinit var mockApi: MockApi

    @Test
    fun testFavoriteEventsHappyFlow() {
        hiltRule.inject()

        sleep(3000)

        checkEventsListVisible()
        checkBrowserIsOpenedWithUrlOnItemClick()
        navigateToFavorites()
        checkFavoritesListIsEmpty()
        navigateToEvents()
        checkFavoriteIconChangesItsStateOnClick()
        navigateToFavorites()
        checkFavoritesListHasItem()
        checkFavoritesListItemDisappearsOnClick()
        navigateToEvents()
        checkFirstItemIsVisibleAfterScrollingAndRefreshingList()
    }

    private fun checkEventsListVisible() {
        onView(withId(R.id.rvEvents)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    private fun checkBrowserIsOpenedWithUrlOnItemClick() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val url = "https://seatgeek.com/us-open-tennis-grounds-admission-september-4-tickets/tennis/2021-09-04-11-am/5349612"

        onView(withId(R.id.rvEvents)).perform(actionOnItemAtPosition<EventsViewHolder>(0, click()))

        sleep(3000)

        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(url)
            )
        )

        val currentPackage: String = device.currentPackageName
        assert(currentPackage == "com.android.chrome")

        device.pressBack()

        sleep(3000)
    }

    private fun navigateToFavorites() {
        onView(withId(R.id.navigation_favorites)).perform(click())
        sleep(3000)
    }

    private fun checkFavoritesListIsEmpty() {
        onView(withText("Add some events to Faves list!")).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        sleep(3000)
    }

    private fun navigateToEvents() {
        onView(withId(R.id.navigation_events)).perform(click())
        sleep(3000)
    }

    private fun checkFavoriteIconChangesItsStateOnClick() {
        onView(withId(R.id.rvEvents))
            .perform(
                actionOnItemAtPosition<FavoritesViewHolder>(
                    0,
                    MyViewAction.clickChildViewWithIdAndCheckDrawableSourceChanged(
                        R.id.ivEventFav,
                        R.drawable.ic_add_fav
                    )
                )
            )

        sleep(3000)

        onView(withId(R.id.rvEvents))
            .perform(
                actionOnItemAtPosition<FavoritesViewHolder>(
                    0,
                    MyViewAction.clickChildViewWithIdAndCheckDrawableSourceChanged(
                        R.id.ivEventFav,
                        R.drawable.ic_fav
                    )
                )
            )

        sleep(3000)

        onView(withId(R.id.rvEvents))
            .perform(
                actionOnItemAtPosition<FavoritesViewHolder>(
                    0,
                    MyViewAction.clickChildViewWithIdAndCheckDrawableSourceChanged(
                        R.id.ivEventFav,
                        R.drawable.ic_add_fav
                    )
                )
            )

        sleep(3000)
    }

    private fun checkFavoritesListHasItem() {
        onView(withId(R.id.rvFavEvents)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.rvFavEvents)).check(matches(hasChildCount(1)))

        sleep(3000)
    }

    private fun checkFavoritesListItemDisappearsOnClick() {
        onView(withId(R.id.rvFavEvents))
            .perform(
                actionOnItemAtPosition<FavoritesViewHolder>(
                    0,
                    MyViewAction.clickChildViewWithIdAndCheckDrawableSourceChanged(
                        R.id.ivFavEventFav,
                        null
                    )
                )
            )
        .check(matches(hasDescendant(withDrawable(R.drawable.ic_fav))))

        sleep(3000)

        checkFavoritesListIsEmpty()
    }

    private fun checkFirstItemIsVisibleAfterScrollingAndRefreshingList() {
        val firstItemText = "TEST Item 1"

        onView(withText(firstItemText)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withRecyclerView(R.id.rvEvents)
            .atPositionOnView(0, R.id.tvEventTitle))
            .check(matches(withText(firstItemText)))

        onView(withId(R.id.rvEvents))
            .perform(actionOnItemAtPosition<EventsViewHolder>(15, scrollTo()))

        sleep(3000)

        onView(withText(firstItemText)).check(doesNotExist())

        onView(withId(R.id.swipeRefreshEvents)).perform(swipeDown())

        sleep(10000)

        onView(withRecyclerView(R.id.rvEvents)
            .atPositionOnView(0, R.id.tvEventTitle))
            .check(matches(withText(firstItemText)))
    }


    private fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as drawable with id $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val context = view.context
            val expectedBitmap = context.getDrawable(id)?.toBitmap()

            return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
        }
    }
}


object MyViewAction {
    fun clickChildViewWithIdAndCheckDrawableSourceChanged(
        clickedViewId: Int,
        @DrawableRes drawableSrc: Int?
    ): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): org.hamcrest.Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(clickedViewId)
                v.performClick()

                drawableSrc?.let { drawableSrc ->
                    val expectedBitmap = view.context.getDrawable(drawableSrc)?.toBitmap()
                    assert(v is ImageView && v.drawable.toBitmap().sameAs(expectedBitmap))
                }
            }
        }
    }
}

