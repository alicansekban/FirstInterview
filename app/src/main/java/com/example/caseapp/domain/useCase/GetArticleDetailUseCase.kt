package com.example.caseapp.domain.useCase

import com.example.caseapp.data.dataSource.LocalDataSource
import com.example.caseapp.domain.BaseUIModel
import com.example.caseapp.domain.Loading
import com.example.caseapp.domain.Success
import com.example.caseapp.domain.mapper.DataMapper
import com.example.caseapp.domain.model.ArticleUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticleDetailUseCase @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val dataMapper: DataMapper
) {

    operator fun invoke(id:Int) : Flow<BaseUIModel<ArticleUIModel>> {

        return flow {
            emit(Loading())

            val article = localDataSource.getArticle(id)

            emit(Success(dataMapper.mapToUIModel(article)))
        }

    }
}