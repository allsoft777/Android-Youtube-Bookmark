package com.owllife.youtubebookmark.core.interactor

import com.owllife.youtubebookmark.domain.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author owllife.dev
 * @since 20. 6. 18
 */
abstract class UseCase<out T, in P> {

    abstract suspend fun run(params: P): ResultData<T>

    suspend fun execute(param: P): ResultData<T> = withContext(Dispatchers.IO) {
        run(param)
    }
}