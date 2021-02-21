package com.ibcorp.helpmeapp.domain.usecase

import com.ibcorp.helpmeapp.domain.repository.NewsRepository
import com.ibcorp.helpmeapp.model.source.Source

class GetSelectedRecordUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(limit:Int,offset:Int): List<Source>? = newsRepository.getSelectedDbSource(limit,offset)
}