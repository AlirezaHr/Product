package com.alirezahr.products.data.remote.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

abstract class BaseDataHandler {
    protected fun <T> performData(
        networkCall: suspend () -> Resource<T>,
    ): Flow<Resource<T>> = flow {
        emit(Resource.loading())
        val responseStatus = networkCall.invoke()
        when (responseStatus.status) {
            Resource.Status.SUCCESS -> {
                emit(responseStatus)
            }

            Resource.Status.ERROR -> {
                emit(responseStatus)
            }

            else -> {}

        }
    }.catch {
        emit(Resource.error(it.message.toString()))
    }
}