package com.alirezahr.products.data.remote.base

import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitConnectionHandler @Inject constructor() : BaseHandleRetrofitConnection() {
    suspend fun <T> execute(call: suspend () -> Response<T>): Resource<T> {
        return makeConnection(call)
    }
}