package com.ibcorp.helpmeapp.data.repository.news_source

import android.util.Log
import com.ibcorp.helpmeapp.model.source.Source
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsCacheDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsLocalDataSource
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsRemoteDataSource
import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import com.ibcorp.helpmeapp.model.allnews.Article
import java.lang.Exception

class NewsRespositoryImpl(
    private val newsCacheDataSource: NewsCacheDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
): NewsRepository {
    override suspend fun getNewsSource(): List<Source>? {
        return getNewsFromCache()
    }

    override suspend fun updateNewsSource(): List<Source>? {
        val newListOfArtist = getNewsFromAPI()
        newsLocalDataSource.clearAll()
        newsLocalDataSource.saveNewsToDB(newListOfArtist)
        newsCacheDataSource.saveNewsToCache(newListOfArtist)
        return newListOfArtist
    }

    override suspend fun getArticleSource(): List<Article>? {
        return getTopHeadlinesFromAPI()
    }

    override suspend fun getDetailSource(id:String): List<Article>? {
       return getDetailSourceFromAPI(id)
    }

    override suspend fun getSelectedDbSource(limit: Int, offset: Int): List<Source>? {
        var list:List<Source> = ArrayList()
        list =  newsLocalDataSource.getSelectedNews(limit,offset)
        return list
    }

    suspend fun getNewsFromAPI(): List<Source> {
        lateinit var newsList: List<Source>
        try {
            val response = newsRemoteDataSource.getNewsFromServer()
            val body = response.body()
            if(body!=null){
                newsList = body.sources
            }
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        return newsList
    }

    suspend fun getDetailSourceFromAPI(id:String):List<Article>{
        lateinit var articleList: List<Article>
        try {
            val response = newsRemoteDataSource.getDetailNewsFromServer(id)
            val body = response.body()
            if(body!=null){
                articleList = body.articles
            }
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        return articleList
    }

    suspend fun getTopHeadlinesFromAPI():List<Article>{
        var articleList: List<Article> = ArrayList()
        try {
            val response = newsRemoteDataSource.getNewsHeadLinesFromServer()
            val body = response.body()
            if(body!=null){
                articleList = body.articles
            }
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        return articleList
    }

    suspend fun getNewsFromDB():List<Source>{
         var newsList: List<Source> = ArrayList()
        try {
            newsList = newsLocalDataSource.getNewsFromDB()
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        if(newsList.size>0){
            return newsList
        }else{
            newsList= getNewsFromAPI()
            newsLocalDataSource.saveNewsToDB(newsList)
        }

        return newsList
    }

    suspend fun getNewsFromCache():List<Source>{
         var newsList: List<Source> = ArrayList()
        try {
            newsList = newsCacheDataSource.getNewsFromCache()
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        if(newsList.size>0){
            return newsList
        }else{
            newsList=getNewsFromDB()
            newsCacheDataSource.saveNewsToCache(newsList)
        }

        return newsList
    }
}
