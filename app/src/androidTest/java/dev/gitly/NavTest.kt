package dev.gitly

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.gitly.view.welcome.WelcomeFragment
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavTest {

    @Test
    fun testNavigationToInGameScreen() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Create a graphical FragmentScenario for the Welcome screen
        val welcomeScenario = launchFragmentInContainer<WelcomeFragment>()

        // Set the NavController property on the fragment
        welcomeScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.parentFragmentManager.executePendingTransactions()
        }

        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.guest_mode)).perform(click())
        assertThat(
            "Nav destination is home page",
            navController.currentDestination?.id,
            IsEqual(R.id.nav_dest_home)
        )
    }
}