package com.ibcorp.helpmeapp.presentation.di.core


import com.ibcorp.helpmeapp.data.api.NewsService
import com.ibcorp.helpmeapp.data.api.QuizService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetroModule(private val baseURL:String) {

    @Singleton
    @Provides
    @Named("news")
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL)
            .build()

    }

    @Singleton
    @Provides
    @Named("quiz")
    fun provideQuizRetrofit():Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://opentdb.com")
            .build()

    }

    @Singleton
    @Provides
    fun getNewsService(@Named("news")retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }

    @Singleton
    @Provides
    fun getQuizService(@Named("quiz")retrofit: Retrofit): QuizService {
        return retrofit.create(QuizService::class.java)
    }
}