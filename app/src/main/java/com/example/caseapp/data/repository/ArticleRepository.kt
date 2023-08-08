package com.example.caseapp.data.repository

import com.example.caseapp.data.dataSource.LocalDataSource
import com.example.caseapp.data.dataSource.RemoteDataSource
import com.example.caseapp.data.local.dbModel.ArticleEntity
import com.example.caseapp.domain.mapper.DataMapper
import com.example.caseapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val dataMapper: DataMapper
) {

    suspend fun fetchData(start: Date?, end: Date?): Flow<ResultWrapper<List<ArticleEntity>>> {
        val startDate =
            start ?: Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -10) }.time
        val endDate = end ?: Date()
        return flow {
            emit(ResultWrapper.Loading)


            when (val apiData = remoteDataSource.getDataFromRemote(startDate, endDate)) {
                is ResultWrapper.GenericError -> {
                    emit(ResultWrapper.GenericError())
                }

                ResultWrapper.Loading -> {}
                ResultWrapper.NetworkError -> {
                    emit(ResultWrapper.NetworkError)
                }

                is ResultWrapper.Success -> {
                    val response = apiData.value.articles
                    if (response?.isNotEmpty() == true) {
                        localDataSource.insertArticleList(response.map { dataMapper.mapToEntity(it!!) })
                    }
                }
            }
            localDataSource.getArticles(startDate,endDate).collect{
                emit(ResultWrapper.Success(it))
            }
        }
    }
}