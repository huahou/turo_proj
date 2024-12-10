package com.mobisigma.pizzabeer.di

import com.mobisigma.pizzabeer.common.Constants
import com.mobisigma.pizzabeer.data.repository.BusinessRepositoryImpl
import com.mobisigma.pizzabeer.data.source.remote.YelpRemoteApi
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun retrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Constants.YELP_BASE_URL)
            .build()
    }

    @Provides
    fun yelpRemoteApi(retrofit: Retrofit): YelpRemoteApi {
        return retrofit.create(YelpRemoteApi::class.java)
    }

    @Provides
    fun businessRepo(yelpRemoteApi: YelpRemoteApi): BusinessRepository {
        return BusinessRepositoryImpl(yelpRemoteApi)
    }

    @Provides
    fun searchUseCase(repo: BusinessRepository): SearchBusinessUseCase {
        return SearchBusinessUseCase(repo)
    }
}