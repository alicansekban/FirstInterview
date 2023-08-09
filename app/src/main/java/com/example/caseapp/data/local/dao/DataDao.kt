package com.example.caseapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caseapp.data.local.model.ArticleEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DataDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleList(articleEntity: List<ArticleEntity>)


    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticle(id: Int): ArticleEntity

    @Query("select * from articles where publishedAt < :end and publishedAt > :start ")
    fun getArticles(start : Date, end:Date): Flow<List<ArticleEntity>>
}