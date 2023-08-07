package com.example.caseapp.data.dataSource

import com.example.caseapp.base.BaseResponse
import com.example.caseapp.data.Service
import com.example.caseapp.utils.ResultWrapper
import com.example.caseapp.utils.safeApiCall
import com.example.caseapp.utils.toParsedString
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: Service) {
    suspend fun getDataFromRemote(startDate: Date, endDate: Date): ResultWrapper<BaseResponse> {
        return safeApiCall(Dispatchers.IO) {
            api.getData(
                from = startDate.toParsedString(),
                to = endDate.toParsedString()
            )
        }
    }
}