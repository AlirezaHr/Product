package com.alirezahr.products.data.remote.base

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val code: Int?) {

    public enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?, code: Int? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, null, code)
        }

        fun <T> error(message: String, data: T? = null, code: Int? = null): Resource<T> {
            return Resource(Status.ERROR, data, message, code)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}