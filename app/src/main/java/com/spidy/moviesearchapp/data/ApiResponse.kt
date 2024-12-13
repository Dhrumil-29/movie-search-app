package com.spidy.moviesearchapp.data

sealed class ApiResponse<T>(val result:T? = null,val error:String?=null) {
    class Success<T>(result: T?):ApiResponse<T>(result)
    class Error<T>(error: String):ApiResponse<T>(null,error)
    class Loading<T>():ApiResponse<T>(null,null)
}