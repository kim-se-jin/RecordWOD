package com.sejin.recordwod.di

import com.sejin.recordwod.repository.UserRepository
import com.sejin.recordwod.repository.remote.RemoteUserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

   @Binds
   fun UserRepository(userRepository: RemoteUserRepository ) : UserRepository



}