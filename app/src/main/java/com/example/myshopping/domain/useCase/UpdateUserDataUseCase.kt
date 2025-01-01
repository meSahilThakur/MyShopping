package com.example.myshopping.domain.useCase

import com.example.myshopping.domain.models.UserDataModel
import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(private val repo: Repo) {
    fun updateUserDataUseCase(userDataModel: UserDataModel) = repo.updateUserProfile(userDataModel)
}