package com.example.myshopping.domain.models

data class UserDataModel(
    var uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val userImage: String = ""
)
