package com.ibcorp.helpmeapp.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibcorp.helpmeapp.domain.usecase.*


class NewsViewModelFactory(
    private val getNewsUseCase: GetNewsUseCase,
    private val updateNewsUseCase: UpdateNewsUseCase,
    private val getArticleUseCase: GetArticleUseCase,
    private val getDetailNewsUseCase: GetDetailNewsUseCase,
    private val getSelectedRecordUseCase: GetSelectedRecordUseCase,
    private val getQuizUsecase: GetQuizUsecase
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(
            getNewsUseCase,
            updateNewsUseCase,
            getArticleUseCase,
            getQuizUsecase,
            getDetailNewsUseCase,
            getSelectedRecordUseCase,
        ) as T
    }
}