package com.ibcorp.helpmeapp.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ibcorp.helpmeapp.domain.usecase.*


class NewsViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val updateNewsUseCase: UpdateNewsUseCase,
    private val getArticleUseCase: GetArticleUseCase,
    private val getQuizUseCase: GetQuizUsecase,
    private val getDetailNewsUseCase: GetDetailNewsUseCase,
    private val getSelectedRecordUseCase: GetSelectedRecordUseCase
): ViewModel() {

    fun getNews() = liveData {
        val artistList = getNewsUseCase.execute()
        emit(artistList)
    }

    fun updateNews() = liveData {
        val artistList = updateNewsUseCase.execute()
        emit(artistList)
    }
    fun getHeadlines() = liveData {
        val articleList =getArticleUseCase.execute()
        emit(articleList)
    }

    fun getQuiz() = liveData {
        val quizList =getQuizUseCase.execute()
        emit(quizList)
    }
    fun getDetailNews(id:String) = liveData {
        val articleList =getDetailNewsUseCase.execute(id)
        emit(articleList)
    }
    fun getSelectedNews(limit:Int,offset:Int)= liveData {
        val sourceList =  getSelectedRecordUseCase.execute(limit,offset)
        emit(sourceList)
    }

}