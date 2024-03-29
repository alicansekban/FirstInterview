package com.example.caseapp.utils

import com.example.caseapp.data.ErrorResponse
import com.example.caseapp.data.ErrorsItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    val errorList = mutableListOf<ErrorsItem>()
                    val error = ErrorsItem(null, "Lütfen internet bağlantınızı kontrol edin.")
                    errorList.add(error)
                    val errorResponse = ErrorResponse(errorList)
                    ResultWrapper.GenericError(null, errorResponse)
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.string()?.let {
            val moshiAdapter = Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}
