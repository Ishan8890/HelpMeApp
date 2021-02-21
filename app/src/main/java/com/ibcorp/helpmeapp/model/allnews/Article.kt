package com.ibcorp.helpmeapp.model.allnews


import androidx.room.Entity
import com.ibcorp.helpmeapp.model.source.Source

@Entity(tableName = "all_news_tb")
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)