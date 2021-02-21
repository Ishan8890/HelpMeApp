package com.ibcorp.helpmeapp.presentation.di.core

import com.ibcorp.helpmeapp.presentation.di.news.NewsSubComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules=[RetroModule::class,
    RoomModule::class,
    AppModule::class,
    UseCaseModule::class,
    RepositoryModule::class,
    RemoteDataModule::class,
    LocalDataModule::class,
    CacheDataModule::class])
interface AppComponent {
    fun newsSubComponent(): NewsSubComponent.Factory
}