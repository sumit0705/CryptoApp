package com.example.cryptoapp.api

import com.example.cryptoapp.models.Crypto
import retrofit2.http.GET

interface ApiRequests {

    @GET("v1/coins")
    suspend fun getCryptoData(): Crypto
}