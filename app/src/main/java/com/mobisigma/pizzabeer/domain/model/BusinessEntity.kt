package com.mobisigma.pizzabeer.domain.model

data class BusinessEntity(
    val id: String,
    val name: String,
    val imageUrl: String,
    val reviewCount: Int,
    val rating: Double,
)