package com.example.cryptoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.models.Crypto
import com.example.cryptoapp.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Creates a new ViewModel instance. */
class MainViewModel(private val repo: Repository) : ViewModel() {

    val _cryptoLiveData = MutableLiveData<Crypto>()
    val cryptoLiveData: LiveData<Crypto>
        get() = _cryptoLiveData

    init {
        fetchCryptos()
    }

    /** This method will fetch the planets data. */
    private fun fetchCryptos() {
        viewModelScope.launch {
            try {
                val response: Crypto = repo.getCrypto()
                _cryptoLiveData.value = response
            } catch (e: Exception) {
                // Got error while fetching activity data e.g. changing internet connection status.
                e.printStackTrace()
                _cryptoLiveData.value = null
            }
        }
    }

    fun refetchCryptos() {
        fetchCryptos()
    }
}
