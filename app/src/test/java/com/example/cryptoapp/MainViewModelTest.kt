package com.example.cryptoapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.cryptoapp.models.Crypto
import com.example.cryptoapp.models.CryptoItem
import com.example.cryptoapp.repo.Repository
import com.example.cryptoapp.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var cryptoObserver: Observer<Crypto>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(repository)
        viewModel.cryptoLiveData.observeForever(cryptoObserver)
    }

    @Test
    fun testFetchCryptoSuccess() = runTest {
        // Arrange
        val cryptoList = arrayListOf(
            CryptoItem(id = "btc-bitcoin", name = "Bitcoin", symbol = "BTC", rank = 1, is_new = false, is_active = true, type = "coin")
        )

        val crypto = Crypto().apply { addAll(cryptoList) }
        Mockito.`when`(repository.getCrypto()).thenReturn(crypto)

        // Act

        viewModel.refetchCryptos()

        // Assert
        Mockito.verify(cryptoObserver).onChanged(crypto)
    }

    @Test
    fun testFetchCryptoEmptyList() = runTest {
        // Arrange
        val crypto = Crypto().apply { addAll(emptyList()) }
        Mockito.`when`(repository.getCrypto()).thenReturn(crypto)

        // Act
        viewModel.refetchCryptos()

        // Assert
        Mockito.verify(cryptoObserver).onChanged(crypto)
    }

    @After
    fun tearDown() {
        viewModel.cryptoLiveData.removeObserver(cryptoObserver)
    }

    class MainCoroutineRule : TestRule {
        override fun apply(base: Statement, description: Description?): Statement {
            return object : Statement() {
                override fun evaluate() {
                    Dispatchers.setMain(Dispatchers.Unconfined)
                    try {
                        base.evaluate()
                    } finally {
                        Dispatchers.resetMain()
                    }
                }
            }
        }
    }
}