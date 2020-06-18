package com.owllife.youtubebookmark.domain

/**
 * @author owllife.dev
 * @since 20. 6. 12
 */
sealed class ResultData<out T> {

    data class Success<out T>(val data: T) : ResultData<T>()
    data class Failure(val exception: Exception) : ResultData<Nothing>()
    object Loading : ResultData<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Error[failure=$exception]"
            Loading -> "Loading"
        }
    }
}

val ResultData<*>.succeeded
    get() = this is ResultData.Success && data != null