package com.ibcorp.helpmeapp.data.api

import com.ibcorp.helpmeapp.model.allnews.NewsModel
import com.ibcorp.helpmeapp.model.source.SourceModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    //https://newsapi.org/v2/sources?apiKey=a40e2d7b33cc4a58bca80e711a1f0117
    @GET("/v2/sources")
    suspend fun getNewsSources(
        @Query(
            "apiKey"
        ) apiKey: String
    ): Response<SourceModel>

    //https://newsapi.org/v2/everything?q=al-jazeera-english&apiKey=a40e2d7b33cc4a58bca80e711a1f0117
    @GET("/v2/everything")
    suspend fun getAllNews(
        @Query("q") id:String,
        @Query("apiKey") apiKey: String
    ): Response<NewsModel>

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
            @Query("country") country:String,
            @Query("apiKey") apiKey:String
    ):Response<NewsModel>

}