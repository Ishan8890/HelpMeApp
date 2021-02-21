package com.ibcorp.helpmeapp.presentation.di.core

import android.content.Context
import androidx.room.Room
import com.ibcorp.helpmeapp.data.db.NewsDb
import com.ibcorp.helpmeapp.data.db.SourceNewsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideRoomDb(context:Context): NewsDb {
        return Room.databaseBuilder(context,NewsDb::class.java,"newsdb")
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(newDb: NewsDb): SourceNewsDao {
        return newDb.newsDao()
    }
}