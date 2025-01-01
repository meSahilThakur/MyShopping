package com.example.myshopping.domain.models

data class Category(
    val name: String = "",
    val imageUri: String = "",
    val date: Long = System.currentTimeMillis()
)
