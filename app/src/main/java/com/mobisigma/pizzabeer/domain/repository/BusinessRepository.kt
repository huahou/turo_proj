package com.mobisigma.pizzabeer.domain.repository

import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.model.Location

interface BusinessRepository {
    suspend fun search(keyword: String, location: Location, offset: Int): List<BusinessEntity>
}