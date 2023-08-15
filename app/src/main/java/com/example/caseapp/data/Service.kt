package com.example.caseapp.data

import com.example.caseapp.base.BaseResponse
import com.example.caseapp.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("everything?q=football&sortedBy=publishedAt&apiKey=${Constant.API_KEY}")
    suspend fun getData(
        @Query("from") from : String?,
        @Query("to") to : String?)  : BaseResponse
}