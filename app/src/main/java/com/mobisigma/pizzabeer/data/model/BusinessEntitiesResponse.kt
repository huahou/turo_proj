package com.mobisigma.pizzabeer.data.model

import com.google.gson.annotations.SerializedName

data class BusinessEntitiesResponse(
    @SerializedName("businesses") val businesses: List<BusinessEntityDto>,
    @SerializedName("total") val total: Int,
)

data class BusinessEntityDto (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("rating") val rating: Double,
)