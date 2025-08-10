package com.alirezahr.products.data.remote.base

import retrofit2.Response

abstract class BaseHandleRetrofitConnection() {
    protected suspend fun <T> makeConnection(call: suspend () -> Response<T>): Resource<T> {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Resource.success(body, response.code())
            }
        } else {
            return Resource.error(
                message = response.errorBody()?.charStream().toString(),
                code = response.code()
            )
        }

        return Resource.error(
            message = "something wrong in caching data from server",
            code = response.code()
        )
    }
}