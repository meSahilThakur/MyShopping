package com.example.myshopping.common

sealed class ResultState<out T>{
    object Loading: ResultState<Nothing>()
    data class Success<T>(val data:T): ResultState<T>()
    data class Error(val error: String): ResultState<Nothing>()
}