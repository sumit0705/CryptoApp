package com.example.cryptoapp.repo

import com.example.cryptoapp.api.ApiClient
import com.example.cryptoapp.models.Crypto

class Repository {

    suspend fun getCrypto(): Crypto {
        return ApiClient.api.getCryptoData()
    }
}