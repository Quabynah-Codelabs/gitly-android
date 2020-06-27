package dev.gitly

import android.net.Uri
import androidx.navigation.NavDeepLink
import androidx.navigation.Navigation
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
class NavTest {
//    @Test
//    fun test() {
//        val navDeepLink = NavDeepLink("scheme://host/path\\?query1={query_value1}&query2={query_value2}")
//        val deepLink = Uri.parse("scheme://host/path?query1=foo_bar&query2=baz")
//
//        val bundle = navDeepLink.getMatchingArguments(deepLink)!!
//        assertTrue(bundle.get("query_value1") == "foo_bar")
//        assertTrue(bundle.get("query_value2") == "baz")
//    }

    @Test
    fun testNavigationToInGameScreen() {
        // Create a TestNavHostController
//        val navController = TestNavHostController(
//            ApplicationProvider.getApplicationContext())
//        navController.setGraph(R.navigation.trivia)

        // Create a graphical FragmentScenario for the TitleScreen
//        val titleScenario = launchFragmentInContainer<TitleScreen>()

        // Set the NavController property on the fragment
//        titleScenario.onFragment { fragment ->
//            Navigation.setViewNavController(fragment.requireView(), navController)
//        }

        // Verify that performing a click changes the NavController’s state
//        onView(ViewMatchers.withId(R.id.play_btn)).perform(ViewActions.click())
//        assertThat(navController.currentDestination?.id).isEqualTo(R.id.in_game)
    }
}