package nam.tran.domain.test.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import nam.tran.domain.LoginUseCase
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.test.util.InstantAppExecutors
import nam.tran.flatform.IApi
import nam.tran.flatform.local.IPreference
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import retrofit2.Call

@RunWith(JUnit4::class)
class LoginUseCaseTest {


    private lateinit var loginUseCase: LoginUseCase
    private val iPreference = Mockito.mock(IPreference::class.java)
    private val service = Mockito.mock(IApi::class.java)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        loginUseCase = LoginUseCase(InstantAppExecutors(), service, iPreference)
    }

    @Test
    fun login() {
        val resource = loginUseCase.login("abc", "123")
        val data = getData(resource)
        MatcherAssert.assertThat(data.data, `is`(null))
    }

    /**
     * extract the latest paged list from the listing
     */
    private fun getData(resourceLiveData: LiveData<Resource<Void>>): Resource<Void> {
        val observer = LoggingObserver<Resource<Void>>()
        resourceLiveData.observeForever(observer)
        MatcherAssert.assertThat(observer.value, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        return observer.value!!
    }

    /**
     * simple observer that logs the latest value it receives
     */
    private class LoggingObserver<T> : Observer<T> {
        var value: T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }

}