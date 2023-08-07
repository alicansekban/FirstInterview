package com.example.caseapp.data.repository

import com.example.caseapp.base.BaseResponse
import com.example.caseapp.data.dataSource.LocalDataSource
import com.example.caseapp.data.dataSource.RemoteDataSource
import com.example.caseapp.data.local.dbModel.ArticleEntity
import com.example.caseapp.domain.mapper.DataMapper
import com.example.caseapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: DataMapper
) {

    fun getData(start: Date? = null, end: Date? = null): Flow<List<ArticleEntity>> {
        val startDate = start ?: Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -10) }.time
        val endDate = end ?: Date()

        return localDataSource.getArticles(startDate, endDate)
    }

    suspend fun getFromRemote(start: Date?, end: Date?) : ResultWrapper<BaseResponse> {
        val startDate = start ?: Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -10) }.time
        val endDate = end ?: Date()
        val remoteResult = remoteDataSource.getDataFromRemote(startDate, endDate)

        if (remoteResult is ResultWrapper.Success) {
            val mappedArticles =
                remoteResult.value.articles?.map { mapper.mapToEntity(it!!) }
            if (!mappedArticles.isNullOrEmpty()) {
                localDataSource.insertArticleList(mappedArticles)
            }
        }

        return remoteResult
    }
}