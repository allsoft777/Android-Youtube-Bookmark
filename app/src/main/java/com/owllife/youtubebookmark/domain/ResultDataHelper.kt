package com.owllife.youtubebookmark.domain

/**
 * @author owllife.dev
 * @since 20. 6. 18
 */
fun makeFailureData(msg: String): ResultData.Failure {
    return ResultData.Failure(Exception(msg))
}