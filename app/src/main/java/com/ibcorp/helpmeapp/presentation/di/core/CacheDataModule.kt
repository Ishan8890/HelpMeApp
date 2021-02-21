package com.ibcorp.helpmeapp.presentation.di.core

import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsCacheDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl.NewsCacheDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheDataModule {
    @Singleton
    @Provides
    fun provideNewsCacheDataSource(): NewsCacheDataSource {
        return NewsCacheDataSourceImpl()
    }

}