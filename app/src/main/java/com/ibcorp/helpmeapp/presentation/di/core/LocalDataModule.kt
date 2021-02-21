package com.ibcorp.helpmeapp.presentation.di.core

import com.ibcorp.helpmeapp.data.db.SourceNewsDao
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsLocalDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl.NewsLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDataModule {

    @Singleton
    @Provides
    fun provideNewsLocalDataSource(sourceNewsDao: SourceNewsDao): NewsLocalDataSource {
        return NewsLocalDataSourceImpl(sourceNewsDao)
    }
}