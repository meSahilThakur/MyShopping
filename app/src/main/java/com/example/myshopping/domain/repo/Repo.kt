package com.example.myshopping.domain.repo

import android.net.Uri
import com.example.myshopping.common.ResultState
import com.example.myshopping.domain.models.Category
import com.example.myshopping.domain.models.ProductDataModel
import com.example.myshopping.domain.models.UserDataModel
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailPassword(userDataModel: UserDataModel): Flow<ResultState<String>>

    fun loginUserWithEmailPassword(userEmail: String, userPassword: String): Flow<ResultState<String>>


    fun getAllCategories(): Flow<ResultState<List<Category>>>

    fun getCategoriesInLimit(): Flow<ResultState<List<Category>>>

    fun getAllProducts(): Flow<ResultState<List<ProductDataModel>>>

    fun getProductsInLimit(): Flow<ResultState<List<ProductDataModel>>>


    fun getProductById(productId: String): Flow<ResultState<ProductDataModel>>

    fun getUserById(uid: String): Flow<ResultState<UserDataModel>>
    fun updateUserProfile(userDataModel: UserDataModel): Flow<ResultState<String>>
    fun updateUserImage(imageUri: Uri): Flow<ResultState<String>>

}