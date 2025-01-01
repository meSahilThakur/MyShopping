package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val repo: Repo) {
    fun getAllProductsUseCase() = repo.getAllProducts()
}