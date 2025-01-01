package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repo: Repo) {

    fun getProductByIdUseCase(productId: String) = repo.getProductById(productId)
}