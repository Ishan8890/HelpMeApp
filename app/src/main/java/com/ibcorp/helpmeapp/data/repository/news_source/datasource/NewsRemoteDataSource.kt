package com.ibcorp.helpmeapp.data.repository.news_source.datasource

import com.ibcorp.helpmeapp.model.allnews.NewsModel
import com.ibcorp.helpmeapp.model.quiz.QuizResponse
import com.ibcorp.helpmeapp.model.source.SourceModel
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getNewsFromServer(): Response<SourceModel>
    suspend fun getNewsHeadLinesFromServer(): Response<NewsModel>
    suspend fun getDetailNewsFromServer(id:String): Response<NewsModel>


}