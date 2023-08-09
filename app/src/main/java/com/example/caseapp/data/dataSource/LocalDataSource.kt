package com.example.caseapp.data.dataSource

import com.example.caseapp.data.local.AppDataBase
import com.example.caseapp.data.local.model.ArticleEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val db : AppDataBase) {

    fun getArticles(start : Date, end: Date) : Flow<List<ArticleEntity>> {
        return db.dataDao().getArticles(start, end)
    }

    suspend fun insertArticleList(list : List<ArticleEntity>) {
        db.dataDao().insertArticleList(list)
    }

    fun getArticle(id: Int) : ArticleEntity {
        return db.dataDao().getArticle(id)
    }
}