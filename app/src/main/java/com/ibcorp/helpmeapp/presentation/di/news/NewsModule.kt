package com.ibcorp.helpmeapp.presentation.di.news

import com.ibcorp.helpmeapp.domain.usecase.*
import com.ibcorp.helpmeapp.presentation.news.NewsViewModelFactory

import dagger.Module
import dagger.Provides

@Module
class NewsModule {
    @NewsScope
    @Provides
    fun provideNewsViewModelFactory(
        getNewsUseCase: GetNewsUseCase,
        updateNewsUseCase: UpdateNewsUseCase,
        getArticleUseCase: GetArticleUseCase,
        getDetailNewsUseCase: GetDetailNewsUseCase,
        getSelectedRecordUseCase: GetSelectedRecordUseCase
    ): NewsViewModelFactory {
        return NewsViewModelFactory(
            getNewsUseCase,
            updateNewsUseCase,
            getArticleUseCase,
            getDetailNewsUseCase,
            getSelectedRecordUseCase
        )
    }

}