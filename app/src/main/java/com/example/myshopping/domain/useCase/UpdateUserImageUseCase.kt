package com.example.myshopping.domain.useCase

import android.net.Uri
import com.example.myshopping.domain.repo.Repo
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(private val repo: Repo) {
    fun updateUserImageUseCase(imageUri: Uri) = repo.updateUserImage(imageUri)
}