package com.ibcorp.helpmeapp.domain.repository

import com.ibcorp.helpmeapp.model.source.Source
import com.ibcorp.helpmeapp.model.allnews.Article

interface NewsRepository {
    suspend fun getNewsSource():List<Source>?
    suspend fun updateNewsSource():List<Source>?
    suspend fun getArticleSource():List<Article>?
    suspend fun getDetailSource(id:String):List<Article>?
    suspend fun getSelectedDbSource(limit:Int,offset:Int):List<Source>?
}