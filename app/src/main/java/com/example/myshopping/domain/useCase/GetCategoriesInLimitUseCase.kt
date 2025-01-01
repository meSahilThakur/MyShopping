package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class GetCategoriesInLimitUseCase @Inject constructor(private val repo: Repo) {
    fun getCategoriesInLimitUseCase() = repo.getCategoriesInLimit()
}