package com.ibcorp.helpmeapp.presentation.di.core

import com.ibcorp.helpmeapp.data.api.NewsService
import com.ibcorp.helpmeapp.data.api.QuizService
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsRemoteDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.QuizDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl.NewsRemoteSourceImpl
import com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl.QuizRemoteSourceImpl
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
    @Singleton
    @Provides
    fun provideQuizRemoteDataSource(quizService: QuizService): QuizDataSource {
        return QuizRemoteSourceImpl(
            quizService
        )
    }

}