package com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl

import com.ibcorp.helpmeapp.model.source.Source
import com.ibcorp.helpmeapp.data.db.SourceNewsDao
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsLocalDataSourceImpl(private val newsDao: SourceNewsDao) : NewsLocalDataSource {
    override suspend fun getNewsFromDB(): List<Source> {
        return newsDao.getNews()
    }

    override suspend fun saveNewsToDB(newsList: List<Source>) {
        CoroutineScope(Dispatchers.IO).launch {
            newsDao.saveNews(newsList)
        }
    }

    override suspend fun clearAll() {
        CoroutineScope(Dispatchers.IO).launch {
            newsDao.deleteAllNews()
        }

    }

    override suspend fun getSelectedNews(limit:Int,offSet:Int): List<Source> {
            return newsDao.getSelectedList(limit,offSet)
    }

}