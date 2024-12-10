package com.mobisigma.pizzabeer.data.source.remote

import com.mobisigma.pizzabeer.data.model.BusinessEntityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpRemoteApi {
    @GET("/search")
    suspend fun searchShop(@Query("term") keyword: String, @Query("location") location: String): Response<BusinessEntityResponse>
}