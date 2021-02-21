package com.ibcorp.helpmeapp.presentation.di


import com.ibcorp.helpmeapp.presentation.di.news.NewsSubComponent

interface Injector {
   fun createNewsSubComponent(): NewsSubComponent
}