package com.openweatherapp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.openweatherapp.dispatcher.MockServerDispatcher
import com.openweatherapp.ui.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var mockServer: MockWebServer

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start(8080)
    }

    @After
    fun tearDown() = mockServer.shutdown()

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }
    @Test
    fun happyCase() {
        mockServer.dispatcher = MockServerDispatcher.ResponseDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        intent.putExtra(
            "MOCK_URL",
            mockServer.url("/").toString()
        )
        rule.launchActivity(intent)

        onView(withId(R.id.shimmer_view_container))
            .check(matches(not(isDisplayed())))

        val request = mockServer.takeRequest();
        Assert.assertEquals("GET",request.method);

        onView(withId(R.id.todays_weather))
            .check(matches(not(isDisplayed())));

    }

    @Test
    fun clickOnItem() {
        mockServer.dispatcher = MockServerDispatcher.ResponseDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        intent.putExtra(
            "MOCK_URL",
            mockServer.url("/").toString()
        )
        rule.launchActivity(intent)


        val request = mockServer.takeRequest();


        onView(withId(R.id.shimmer_view_container))
            .check(matches(not(isDisplayed())))

        Assert.assertEquals("GET",request.method);

        onView(withId(R.id.todays_weather))
            .check(matches(not(isDisplayed())));

        try {
            Thread.sleep(2000)
        } catch (error: InterruptedException) {
        }

        onView(withId(R.id.retry))
            .check(matches(isDisplayed()))
            .perform(click())



    }

    @Test
    fun whenOkayButtonIsPressedAndAmountIsEmptyTipIsEmpty() {
        mockServer.dispatcher = MockServerDispatcher.ResponseDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        intent.putExtra(
            "MOCK_URL",
            mockServer.url("/").toString()
        )
        rule.launchActivity(intent)

        onView(withId(R.id.shimmer_view_container))
            .check(matches(not(ViewMatchers.isDisplayed())))



    }

    @Test
    fun swipeRefresh() {
        mockServer.dispatcher = MockServerDispatcher.ResponseDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        intent.putExtra(
            "MOCK_URL",
            mockServer.url("/").toString()
        )
        rule.launchActivity(intent)


        onView(withId(R.id.swipe_refresh_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown());
        onView(withId(R.id.todays_weather)).check(matches(isDisplayed()));


    }
    @Test
    fun unHappyCase() {


        mockServer.dispatcher = MockServerDispatcher.ErrorDispatcher()
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        intent.putExtra(
            "MOCK_URL",
            mockServer.url("Hey! I am Mock URL/").toString()
        )
        rule.launchActivity(intent)
        //  failure layout visible
        onView(withId(R.id.error_layout))
            .check(matches(ViewMatchers.isDisplayed()))

    }

    @Test
    @Throws(Exception::class)
    fun testHttp404Error() {
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainActivity::class.java
        )
        intent.putExtra(
            "MOCK_URL",
            mockServer.url("/").toString()
        )
        rule.launchActivity(intent)
        mockServer.enqueue(MockResponse().setResponseCode(404))
        onView(withId(R.id.error_layout))
            .check(matches(isDisplayed()))
    }
}