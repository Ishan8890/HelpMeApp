package com.ibcorp.helpmeapp.data.repository.news_source.datasource

import com.ibcorp.helpmeapp.model.source.Source

interface NewsLocalDataSource {
    suspend fun getNewsFromDB():List<Source>
    suspend fun saveNewsToDB(artists:List<Source>)
    suspend fun clearAll()
    suspend fun getSelectedNews(limit:Int,offSet:Int):List<Source>
}