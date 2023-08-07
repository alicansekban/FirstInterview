package com.example.caseapp.domain.mapper

import com.example.caseapp.base.ArticlesItem
import com.example.caseapp.data.local.dbModel.ArticleEntity
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.utils.toDate
import java.util.Date
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
            author = item.author,
            urlToImage = item.urlToImage,
            description = item.description,
            sourceName = item.sourceName,
            sourceId = item.sourceId,
            title = item.title,
            url = item.url,
            content = item.content
        )
    }

    fun mapListToUIModel(items: List<ArticleEntity>): List<ArticleUIModel> {
        return items.map { item ->
            ArticleUIModel(
                publishedAt = item.publishedAt,
                author = item.author,
                urlToImage = item.urlToImage,
                description = item.description,
                sourceName = item.sourceName,
                sourceId = item.sourceId,
                title = item.title,
                url = item.url,
                content = item.content
            )
        }
    }
}