package com.ibcorp.helpmeapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.ibcorp.helpmeapp.presentation.di.Injector
import com.ibcorp.helpmeapp.presentation.di.core.*
import com.ibcorp.helpmeapp.presentation.di.news.NewsSubComponent

class App : Application(),Injector {
    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .retroModule(RetroModule(BuildConfig.BASE_URL))
            .remoteDataModule(RemoteDataModule(BuildConfig.API_KEY))
            .build()
    }

    override fun createNewsSubComponent(): NewsSubComponent {
        return appComponent.newsSubComponent().create()
    }
}