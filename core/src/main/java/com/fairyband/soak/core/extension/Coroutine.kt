package com.fairyband.soak.core.extension

import kotlinx.coroutines.CancellationException

suspend inline fun <R> suspendRunCatching(
    block: suspend () -> R,
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}