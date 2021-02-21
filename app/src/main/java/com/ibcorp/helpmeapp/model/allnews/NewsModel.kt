package com.ibcorp.helpmeapp.model.allnews


data class NewsModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)