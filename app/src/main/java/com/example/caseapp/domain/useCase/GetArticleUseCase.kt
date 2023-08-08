package com.example.caseapp.domain.useCase

import com.example.caseapp.data.repository.ArticleRepository
import com.example.caseapp.domain.BaseUIModel
import com.example.caseapp.domain.Error
import com.example.caseapp.domain.Loading
import com.example.caseapp.domain.Success
import com.example.caseapp.domain.mapper.DataMapper
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val dataMapper: DataMapper
) {

    operator fun invoke(
        startDate: Date?,
        endDate: Date?
    ): Flow<BaseUIModel<List<ArticleUIModel>>> {
        return flow {
            emit(Loading())

            articleRepository.fetchData(startDate,endDate).collect{
                when (it) {
                    is ResultWrapper.GenericError -> { emit(Error("something went wrong"))}
                    ResultWrapper.Loading -> {}
                    ResultWrapper.NetworkError -> { emit(Error("could not retrive data from network"))}
                    is ResultWrapper.Success -> emit(Success(dataMapper.mapListToUIModel(it.value)))
                }
            }
        }
    }
}