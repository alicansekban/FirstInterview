package com.example.caseapp.data

import com.example.caseapp.base.BaseResponse
import com.example.caseapp.utils.ResultWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("everything?q=football&sortedBy=publishedAt&apiKey=ece784dcf83847bbaf09e5e18653f61e")
    suspend fun getData(
        @Query("from") from : String?,
        @Query("to") to : String?)  : BaseResponse
}