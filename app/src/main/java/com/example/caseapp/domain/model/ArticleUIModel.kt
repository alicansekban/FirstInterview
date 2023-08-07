package com.example.caseapp.domain.model

import java.util.Date

data class ArticleUIModel(
    val id : Int,
    val publishedAt: Date?,
    val author: String?,
    val urlToImage: String?,
    val description: String?,
    val sourceName: String?,
    val sourceId: String?,
    val title: String?,
    val url: String?,
    val content: String?
)
