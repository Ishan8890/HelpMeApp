package com.ibcorp.helpmeapp.presentation.di.core

import com.ibcorp.helpmeapp.data.api.NewsService
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsRemoteDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl.NewsRemoteSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteDataModule(private val apiKey: String) {
    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(newsdbService: NewsService): NewsRemoteDataSource {
        return NewsRemoteSourceImpl(
            newsdbService, apiKey
        )
    }

}