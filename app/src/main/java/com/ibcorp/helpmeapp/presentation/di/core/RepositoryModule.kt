package com.ibcorp.helpmeapp.presentation.di.core

import com.ibcorp.helpmeapp.data.repository.news_source.NewsRespositoryImpl
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsCacheDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsLocalDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsRemoteDataSource
import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        newsRemoteDatasource: NewsRemoteDataSource,
        newsLocalDataSource: NewsLocalDataSource,
        newsCacheDataSource: NewsCacheDataSource
    ): NewsRepository {

        return NewsRespositoryImpl(
            newsCacheDataSource,
            newsLocalDataSource,
            newsRemoteDatasource
        )
    }

}