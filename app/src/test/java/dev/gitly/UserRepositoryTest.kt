package dev.gitly

import android.content.Context
import androidx.lifecycle.liveData
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.data.User
import dev.gitly.model.repositories.UserRepository
import dev.gitly.model.repositories.UserRepositoryImpl
import dev.gitly.model.sources.local.UserLocalDataSource
import dev.gitly.model.sources.local.UserLocalDataSourceImpl
import dev.gitly.model.sources.local.daos.UserDao
import dev.gitly.model.sources.remote.UserRemoteDataSource
import dev.gitly.model.sources.remote.UserRemoteDataSourceImpl
import dev.gitly.model.sources.remote.service.PagingRequest
import dev.gitly.model.sources.remote.service.WebService
import dev.gitly.util.TestCoroutineRule
import dev.gitly.util.observeForTesting
import dev.gitly.util.shouldEqual
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var webService: WebService

    @Mock
    private lateinit var mockContext: Context

    private lateinit var authPrefs: AuthPrefs
    private lateinit var localDataSource: UserLocalDataSource
    private lateinit var remoteDataSource: UserRemoteDataSource
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        authPrefs = AuthPrefs(mockContext)
        localDataSource = UserLocalDataSourceImpl(userDao, authPrefs)
        remoteDataSource = UserRemoteDataSourceImpl(webService, authPrefs)
        userRepository = UserRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `should succeed when requestMentors returns proper data`() {
        val data = mutableListOf<User>()
        val request = PagingRequest(1, 10)

        // Run blocking test
        testCoroutineRule.runBlockingTest {
            delay(3_000)
            // Given
            `when`(webService.requestMentors(request)).thenReturn(data)

            // When
            userRepository.getUsers(1, 10)

            // Then
            verify(webService).requestMentors(request)
        }

    }

    @Test
    fun `emit some data`() {
        // Given
        val subject = liveData {
            emit(1)
            delay(1_000)
            emit(2)
        }

        // When
        subject.observeForTesting {
            // Then
            println(subject.value shouldEqual 1)
            // delay for a virtual 1 sec
            println(subject.value shouldEqual 2)
        }
    }


}
