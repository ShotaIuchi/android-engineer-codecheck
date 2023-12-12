package jp.co.yumemi.android.codeCheck
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailRepoFragmentTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(TopActivity::class.java)

    @Test
    fun `表示内容`() {
        onView(withId(R.id.searchInputText))
            .perform(click())
            .perform(typeText("query"), closeSoftKeyboard())
            .perform(pressImeActionButton())

        // 外部APIをDIしてるのでスリープで対応
        Thread.sleep(1000)

        onView(withText("ACCOUNT/REPOSITORY2"))
            .perform(click())

        onView(withText("ACCOUNT/REPOSITORY2"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withText("Written in LANGUAGE2"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withText("2 stars"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withText("2 watchers"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withText("2 forks"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withText("2 open issues"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
