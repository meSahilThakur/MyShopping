package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repo: Repo)  {
    fun getUserByIdUseCase(uid: String) = repo.getUserById(uid)
}