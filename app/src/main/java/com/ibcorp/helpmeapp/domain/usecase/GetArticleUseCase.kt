package com.ibcorp.helpmeapp.domain.usecase

import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import com.ibcorp.helpmeapp.model.allnews.Article


class GetArticleUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute():List<Article>? = newsRepository.getArticleSource()
}