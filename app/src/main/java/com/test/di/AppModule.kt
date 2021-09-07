package com.test.di

import com.test.dispatcher.AppDispatchers
import com.test.dispatcher.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDispatchers(): DispatchersProvider {
        return AppDispatchers.Default
    }
}