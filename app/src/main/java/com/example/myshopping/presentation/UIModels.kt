package com.example.myshopping.presentation

import com.example.myshopping.data.repoImpl.RepoImpl
import com.example.myshopping.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UIModels {

    @Provides
    fun provideRepo(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth, firebaseStorage: FirebaseStorage): Repo{
        return RepoImpl(firestore, firebaseAuth, firebaseStorage)
    }

}