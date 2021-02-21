package com.ibcorp.helpmeapp.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.ibcorp.helpmeapp.model.source.Source

@Database(entities = [Source::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDb : RoomDatabase(){
    abstract fun newsDao(): SourceNewsDao
}