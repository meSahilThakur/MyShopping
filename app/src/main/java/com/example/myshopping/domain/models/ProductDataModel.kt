package com.example.myshopping.domain.models

data class ProductDataModel(
    var productId: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val finalPrice: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val date: Long = System.currentTimeMillis(),
    val createBy: String = "",
    val availableUnits: Int = 0
)
