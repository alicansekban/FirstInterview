package com.example.caseapp.domain.model

import java.util.Date

data class ArticleUIModel(
    val id : Int,
    val publishedAt: Date?,
    val urlToImage: String?,
    val description: String?,
    val title: String?,
    val content: String?
)
