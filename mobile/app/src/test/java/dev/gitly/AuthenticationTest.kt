package dev.gitly

import android.content.Context
import dev.gitly.core.prefs.AuthPrefs
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

// https://proandroiddev.com/how-to-unit-test-code-with-coroutines-50c1640f6bef
@RunWith(MockitoJUnitRunner::class)
class AuthenticationTest {

    @Mock
    private lateinit var mockContext: Context

    lateinit var authPrefs: AuthPrefs

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        // init prefs
        authPrefs = AuthPrefs(mockContext)
    }

    @Test
    fun verifyLoginState() {
        authPrefs.logout()
    }
}