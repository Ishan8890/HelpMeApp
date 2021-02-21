package com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl


import com.ibcorp.helpmeapp.model.allnews.NewsModel
import com.ibcorp.helpmeapp.data.api.NewsService
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsRemoteDataSource
import com.ibcorp.helpmeapp.model.source.SourceModel
import retrofit2.Response

class NewsRemoteSourceImpl(private val apiService: NewsService, private val apiKey:String) :
    NewsRemoteDataSource {
    override suspend fun getNewsFromServer(): Response<SourceModel> = apiService.getNewsSources(apiKey)
    override suspend fun getNewsHeadLinesFromServer(): Response<NewsModel> = apiService.getTopHeadlines("in",apiKey)
    override suspend fun getDetailNewsFromServer(id:String): Response<NewsModel> {
       return apiService.getAllNews(id,apiKey)
    }



}