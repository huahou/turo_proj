package com.mobisigma.pizzabeer.data.source.remote

import com.mobisigma.pizzabeer.data.model.BusinessEntitiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpRemoteApi {
    @GET("/v3/businesses/search")
    suspend fun searchBusiness(
        @Query("term") keyword: String,
        @Query("location") location: String?,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("offset") offset: Int
    ): Response<BusinessEntitiesResponse>
}