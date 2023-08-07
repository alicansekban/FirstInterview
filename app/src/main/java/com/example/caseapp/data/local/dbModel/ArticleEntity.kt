package com.example.caseapp.data.local.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val publishedAt: Date? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val sourceName: String? = null,
    val sourceId: String? = null,
    val title: String? = null,
    val url: String? = null,
    val content: String? = null
)

data class Source(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "id")
    val id: String? = null
)

