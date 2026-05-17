package com.vu.androidbasicapp.di

import com.vu.androidbasicapp.data.repository.AuthRepository
import com.vu.androidbasicapp.data.repository.AuthRepositoryImpl
import com.vu.androidbasicapp.data.repository.DashboardRepository
import com.vu.androidbasicapp.data.repository.DashboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        impl: DashboardRepositoryImpl
    ): DashboardRepository
}