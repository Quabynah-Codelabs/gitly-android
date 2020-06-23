package dev.gitly

import androidx.test.core.app.ApplicationProvider
import dev.gitly.core.prefs.AuthPrefs
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthenticationTest {

    lateinit var authPrefs: AuthPrefs

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        // init prefs
        authPrefs = AuthPrefs(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun verifyLoginState() {
//        `when`(authPrefs.logout()).thenReturn(debugger("Logged out"))
//        verify(authPrefs).logout()
        authPrefs.logout()
    }
}