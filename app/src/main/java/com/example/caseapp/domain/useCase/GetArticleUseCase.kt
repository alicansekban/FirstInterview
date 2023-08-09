package com.example.caseapp.domain.useCase

import com.example.caseapp.data.repository.ArticleRepository
import com.example.caseapp.domain.model.BaseUIModel
import com.example.caseapp.domain.model.Error
import com.example.caseapp.domain.model.Loading
import com.example.caseapp.domain.model.Success
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

            // repositoryden çektiğimiz result'u uimodel'imize çevirip gerekli dataları mapper yapıyoruz.
            articleRepository.fetchData(startDate,endDate).collect{ data ->
                when (data) {
                    is ResultWrapper.GenericError -> { emit(Error("something went wrong"))}
                    ResultWrapper.Loading -> {}
                    ResultWrapper.NetworkError -> { emit(Error("could not retrive data from network"))}
                    is ResultWrapper.Success -> emit(Success(data.value.map { article -> dataMapper.mapToUIModel(article) }))
                }
            }
        }
    }
}