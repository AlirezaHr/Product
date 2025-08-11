package com.alirezahr.products.data.remote.base

enum class Error {
    SOMETHING_WRONG,
    UNKNOWN_ERROR
}

fun mapError(message: String?): Error {
    return when {
        message?.contains("Unable to resolve host", ignoreCase = true) == true -> Error.SOMETHING_WRONG
        else -> Error.UNKNOWN_ERROR
    }
}