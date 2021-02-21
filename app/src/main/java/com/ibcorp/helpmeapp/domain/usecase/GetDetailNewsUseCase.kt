package com.ibcorp.helpmeapp.domain.usecase

import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import com.ibcorp.helpmeapp.model.allnews.Article


class GetDetailNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(id:String):List<Article>? {
        return newsRepository.getDetailSource(id)
    }
}