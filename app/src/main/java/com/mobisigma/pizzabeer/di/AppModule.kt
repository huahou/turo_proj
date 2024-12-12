package com.mobisigma.pizzabeer.di

import android.content.Context
import com.mobisigma.pizzabeer.common.Constants
import com.mobisigma.pizzabeer.data.network.AuthInterceptor
import com.mobisigma.pizzabeer.data.network.sslPinning
import com.mobisigma.pizzabeer.data.repository.BusinessRepositoryImpl
import com.mobisigma.pizzabeer.data.source.remote.YelpRemoteApi
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun okHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(Constants.AUTH_TOKEN))
                .sslPinning(context)
                .build()
    }

    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Constants.YELP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
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