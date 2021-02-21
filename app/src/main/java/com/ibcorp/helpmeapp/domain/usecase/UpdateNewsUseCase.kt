package com.ibcorp.helpmeapp.domain.usecase

import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import com.ibcorp.helpmeapp.model.source.Source

class UpdateNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute():List<Source>? = newsRepository.updateNewsSource()
}