package com.example.caseapp.domain.useCase

import com.example.caseapp.data.repository.ArticleRepository
import com.example.caseapp.domain.BaseUIModel
import com.example.caseapp.domain.Loading
import com.example.caseapp.domain.Success
import com.example.caseapp.domain.mapper.DataMapper
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val repository: ArticleRepository,
    private val dataMapper: DataMapper
) {
    operator fun invoke(
        startDate: Date?,
        endDate: Date?
    ): Flow<BaseUIModel<List<ArticleUIModel>>> {
        return flow {
            emit(Loading())

            when (repository.getFromRemote(startDate, endDate)) {
                is ResultWrapper.GenericError -> {
                    emit(com.example.caseapp.domain.Error("Could not retrive the data from network"))
                }

                ResultWrapper.NetworkError -> {
                    emit(com.example.caseapp.domain.Error("Network error occured"))
                }

                else -> {

                }
            }

            repository.getData(startDate, endDate).collect {
                emit(Success(dataMapper.mapListToUIModel(it)))
            }
        }
    }
}
