package com.example.caseapp.domain.mapper

import com.example.caseapp.base.ArticlesItem
import com.example.caseapp.data.local.dbModel.ArticleEntity
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.utils.toDate
import javax.inject.Inject

class DataMapper @Inject constructor() {

    fun mapToEntity(item: ArticlesItem): ArticleEntity {
        return ArticleEntity(
            publishedAt = item.publishedAt?.toDate(),
            author = item.author,
            urlToImage = item.urlToImage,
            description = item.description,
            sourceName = item.source?.name,
            sourceId = item.source?.id,
            title = item.title,
            url = item.url,
            content = item.content
        )
    }

    fun mapToUIModel(item: ArticleEntity): ArticleUIModel {
        return ArticleUIModel(
            publishedAt = item.publishedAt,
            urlToImage = item.urlToImage,
            description = item.description,
            title = item.title,
            content = item.content,
            id = item.id
        )
    }
}