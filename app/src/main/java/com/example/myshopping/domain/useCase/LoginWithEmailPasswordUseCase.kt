package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class LoginWithEmailPasswordUseCase @Inject constructor(private val repo: Repo) {
    fun loginWithEmailPasswordUseCase(userEmail: String, userPassword: String) = repo.loginUserWithEmailPassword(userEmail, userPassword)
}