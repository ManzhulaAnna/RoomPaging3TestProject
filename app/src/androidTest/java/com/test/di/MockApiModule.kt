package com.test.di

import com.google.gson.Gson
import com.test.api.MockApi
import com.test.api.helper.MockNetworkFlavour
import com.test.api.helper.NetworkFlavourProvider
import com.test.data.network.EventsService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkDataModule::class]
)
abstract class MockApiModule {

    @Binds
    abstract fun bindMockApi(mockApi: MockApi): EventsService

    companion object {
        @Provides
        @Singleton
        fun provideApi(
            gson: Gson,
            networkFlavourProvider: NetworkFlavourProvider
        ): MockApi {
            return MockApi(gson, networkFlavourProvider)
        }

        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }

        @Provides
        @Singleton
        fun provideMockNetworkFlavour(): NetworkFlavourProvider {
            return MockNetworkFlavour()
        }
    }
}
