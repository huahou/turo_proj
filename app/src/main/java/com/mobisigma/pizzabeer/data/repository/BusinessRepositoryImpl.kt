package com.mobisigma.pizzabeer.data.repository

import com.mobisigma.pizzabeer.data.source.remote.YelpRemoteApi
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository

class BusinessRepositoryImpl constructor(val remoteApi: YelpRemoteApi): BusinessRepository {
    override suspend fun search(keyword: String, location: String): List<BusinessEntity> {
        TODO("Not yet implemented")
    }
}