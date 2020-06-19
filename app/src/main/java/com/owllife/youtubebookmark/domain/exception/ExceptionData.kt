package com.owllife.youtubebookmark.domain.exception

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
sealed class ExceptionData {
    data class NoErrException(val msg: String) : Exception(msg)
    data class FireStoreException(val msg: String) : Exception(msg)
}