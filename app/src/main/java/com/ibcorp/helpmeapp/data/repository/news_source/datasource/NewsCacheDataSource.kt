package com.ibcorp.helpmeapp.data.repository.news_source.datasource

import com.ibcorp.helpmeapp.model.source.Source


interface NewsCacheDataSource {
    suspend fun getNewsFromCache():List<Source>
    suspend fun saveNewsToCache(artists:List<Source>)

}