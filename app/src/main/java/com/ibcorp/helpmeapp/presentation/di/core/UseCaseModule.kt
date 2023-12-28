package com.ibcorp.helpmeapp.presentation.di.core


import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import com.ibcorp.helpmeapp.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository): GetNewsUseCase {
        return GetNewsUseCase(newsRepository)
    }
    @Provides
    fun provideUpdateNewsUseCase(newsRepository: NewsRepository): UpdateNewsUseCase {
        return UpdateNewsUseCase(newsRepository)
    }
    @Provides
    fun provideGetArticleUseCase(newsRepository: NewsRepository): GetArticleUseCase {
        return GetArticleUseCase(newsRepository)
    }
    @Provides
    fun provideGetQuizUseCase(newsRepository: NewsRepository): GetQuizUsecase {
        return GetQuizUsecase(newsRepository)
    }
    @Provides
    fun provideGetDetailUseCase(newsRepository: NewsRepository): GetDetailNewsUseCase {
        return GetDetailNewsUseCase(newsRepository)
    }

    @Provides
    fun provideGetSelectedRecordUseCase(newsRepository: NewsRepository): GetSelectedRecordUseCase {
        return GetSelectedRecordUseCase(newsRepository)
    }

}