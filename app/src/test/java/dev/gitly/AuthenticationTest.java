package dev.gitly;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.gitly.core.prefs.AuthPrefs;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class AuthenticationTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    AuthPrefs authPrefs;

    @Test
    public void verifyLogin() {
        assertNull(authPrefs.getUserId());
        verify(authPrefs).logout();
    }

}
