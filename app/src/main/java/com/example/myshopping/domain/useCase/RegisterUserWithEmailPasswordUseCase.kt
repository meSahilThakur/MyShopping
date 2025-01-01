package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.models.UserDataModel
import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class RegisterUserWithEmailPasswordUseCase @Inject constructor(private val repo: Repo) {
    fun registerUserWithEmailPasswordUseCase(userDataModel: UserDataModel) = repo.registerUserWithEmailPassword(userDataModel)
}