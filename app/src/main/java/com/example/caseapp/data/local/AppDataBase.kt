package com.example.caseapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.caseapp.data.local.dao.DataDao
import com.example.caseapp.data.local.model.ArticleEntity
import com.example.caseapp.data.local.model.Source

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(SourceTypeConverter::class, Converters::class)
abstract class AppDataBase : RoomDatabase(){
    abstract fun dataDao() : DataDao
}


class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return source?.let {
            "${it.name},${it.id}"
        }
    }

    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        if (sourceString == null) return null
        val parts = sourceString.split(",")
        return if (parts.size == 2) {
            Source(parts[0], parts[1])
        } else {
            null
        }
    }
}