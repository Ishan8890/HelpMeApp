package com.ibcorp.helpmeapp.data.repository.news_source.datasourceImpl

import com.ibcorp.helpmeapp.model.source.Source
import com.ibcorp.helpmeapp.data.repository.news_source.datasource.NewsCacheDataSource


class NewsCacheDataSourceImpl : NewsCacheDataSource {

    private var artistList = ArrayList<Source>()

    override suspend fun getNewsFromCache(): List<Source> {
        return artistList
    }

    override suspend fun saveNewsToCache(artists: List<Source>) {

        artistList.clear()
        artistList = ArrayList(artists)
    }
}