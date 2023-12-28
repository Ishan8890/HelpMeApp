package com.ibcorp.helpmeapp.domain.repository

import com.ibcorp.helpmeapp.model.source.Source
import com.ibcorp.helpmeapp.model.allnews.Article
import com.ibcorp.helpmeapp.model.quiz.QuizResponse
import com.ibcorp.helpmeapp.model.quiz.Result

interface NewsRepository {
    suspend fun getNewsSource():List<Source>?
    suspend fun updateNewsSource():List<Source>?
    suspend fun getArticleSource():List<Article>?
    suspend fun getQuizSource():List<Result>?
    suspend fun getDetailSource(id:String):List<Article>?
    suspend fun getSelectedDbSource(limit:Int,offset:Int):List<Source>?
}