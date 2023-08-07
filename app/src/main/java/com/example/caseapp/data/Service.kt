package com.example.caseapp.data

import com.example.caseapp.base.BaseResponse
import com.example.caseapp.utils.ResultWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("everything?q=football&sortedBy=publishedAt&apiKey=ae68088e70d04639b4950bdc9d546924")
    suspend fun getData(
        @Query("from") from : String?,
        @Query("to") to : String?)  : BaseResponse
}