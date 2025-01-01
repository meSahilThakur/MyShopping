package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repo: Repo) {

    fun getAllCategoriesUseCase() = repo.getAllCategories()
}