package com.mobisigma.pizzabeer.data.mapper

import com.mobisigma.pizzabeer.data.model.BusinessEntitiesResponse
import com.mobisigma.pizzabeer.data.model.BusinessEntityDto
import com.mobisigma.pizzabeer.domain.model.BusinessEntity

fun BusinessEntitiesResponse.toBusinessEntities(): List<BusinessEntity> {
    return this.businesses.map { it.toBusinessEntity() }
}

fun BusinessEntityDto.toBusinessEntity(): BusinessEntity =
    BusinessEntity(id = this.id)