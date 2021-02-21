package com.ibcorp.helpmeapp.presentation.di.core

import android.content.Context
import com.ibcorp.helpmeapp.presentation.di.news.NewsSubComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [NewsSubComponent::class])
class AppModule(private val context: Context) {

 @Singleton
 @Provides
 fun provideApplicationContext():Context{
     return context.applicationContext
 }

}