package com.mobisigma.pizzabeer.domain.repository

import com.mobisigma.pizzabeer.domain.model.BusinessEntity

interface BusinessRepository {
    suspend fun search(keyword: String, location: String): List<BusinessEntity>
}