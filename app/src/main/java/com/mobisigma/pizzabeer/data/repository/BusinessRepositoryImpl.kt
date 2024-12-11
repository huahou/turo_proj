package com.mobisigma.pizzabeer.data.repository

import com.mobisigma.pizzabeer.data.mapper.toBusinessEntities
import com.mobisigma.pizzabeer.data.source.remote.YelpRemoteApi
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository

class BusinessRepositoryImpl constructor(private val remoteApi: YelpRemoteApi): BusinessRepository {
    override suspend fun search(keyword: String, location: String): List<BusinessEntity> {
        //TODO: add database logic here
        val response = remoteApi.searchShop(keyword, location)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toBusinessEntities()
        } else {
            throw Exception("Error occurred!")
        }
    }
}