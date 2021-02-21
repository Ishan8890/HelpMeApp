package com.ibcorp.helpmeapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ibcorp.helpmeapp.model.source.Source


@Dao
interface SourceNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(artists : List<Source>)

    @Query("DELETE FROM news_sources_tb")
    suspend fun deleteAllNews()

    @Query("SELECT * FROM news_sources_tb")
    suspend fun getNews():List<Source>

    @Query("SELECT * FROM news_sources_tb ORDER BY id  LIMIT :id,:offSet")
    suspend fun getSelectedList(id:Int,offSet:Int):List<Source>
}