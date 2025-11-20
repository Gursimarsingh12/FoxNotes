package com.app.foxtasks.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> safeFlow(block: suspend () -> T): Flow<Resource<T>>{
    return flow {
        emit(Resource.Loading)
        try {
            val data = block()
            emit(Resource.Success(data))
        }catch (e: Exception){
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }
}