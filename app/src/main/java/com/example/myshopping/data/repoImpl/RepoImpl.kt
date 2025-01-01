package com.example.myshopping.data.repoImpl

import android.R.attr.data
import android.net.Uri
import android.util.Log
import coil.util.CoilUtils.result
import com.example.myshopping.common.COLLECTION_CATEGORY
import com.example.myshopping.common.COLLECTION_PRODUCT
import com.example.myshopping.common.COLLECTION_USERS
import com.example.myshopping.common.ResultState
import com.example.myshopping.domain.models.Category
import com.example.myshopping.domain.models.ProductDataModel
import com.example.myshopping.domain.models.UserDataModel
import com.example.myshopping.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) : Repo {
    override fun registerUserWithEmailPassword(userDataModel: UserDataModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseAuth.createUserWithEmailAndPassword(userDataModel.email, userDataModel.password)
//            .addOnSuccessListener {
//                firestore.collection("USERS").document(it.user?.uid.toString())
//                    .set(userDataModel).addOnSuccessListener {
//                        trySend(ResultState.Success("User Registered Successfully"))
//                        }.addOnFailureListener {
//                            trySend(ResultState.Error(it.toString()))
//                        }
//                }.addOnFailureListener {
//                    trySend(ResultState.Error(it.toString()))
//                }
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val userId = it.result.user?.uid.toString()
                    firestore.collection(COLLECTION_USERS).document(userId)
                        .set(userDataModel).addOnSuccessListener {
                            trySend(ResultState.Success("User Registered Successfully"))
                            Log.d("REGIS", "registerUserWithEmailPassword: User Registered Successfully")
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.toString()))
                        }
                }else{
                    trySend(ResultState.Error(it.exception.toString()))
                }
            }
        awaitClose {
            close()
        }
    }

    override fun loginUserWithEmailPassword(
        userEmail: String,
        userPassword: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {
            trySend(ResultState.Success("User Logged In Successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override fun getAllCategories(): Flow<ResultState<List<Category>>> = callbackFlow {
        trySend(ResultState.Loading)

        firestore.collection(COLLECTION_CATEGORY).get()
            .addOnSuccessListener {
                val categoryData = it.documents.mapNotNull {
                    it.toObject(Category::class.java)
                }
                trySend(ResultState.Success(categoryData))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

        awaitClose {
            close()
        }
    }

    override fun getCategoriesInLimit(): Flow<ResultState<List<Category>>> = callbackFlow {
        trySend(ResultState.Loading)

        firestore.collection(COLLECTION_CATEGORY).limit(4).get()
            .addOnSuccessListener {
                val categoryLimitData = it.documents.mapNotNull {
                    it.toObject(Category::class.java)
                }
                trySend(ResultState.Success(categoryLimitData))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

        awaitClose {
            close()
        }
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(COLLECTION_PRODUCT).get()
            .addOnSuccessListener { snapshot ->
                val productData = snapshot.documents.mapNotNull { document->
                    val productId = document.id     // Get productId from document ID
                    val productObject = document.toObject(ProductDataModel::class.java)
                    productObject?.productId = productId
//                    Log.d("Debug", "getAllProducts: ${productObject}")
                    productObject
                }
                trySend(ResultState.Success(productData))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getProductsInLimit(): Flow<ResultState<List<ProductDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(COLLECTION_PRODUCT).limit(4).get()
            .addOnSuccessListener { result ->
                val productLimitData = result.documents.mapNotNull { document ->
                    val productId = document.id     // Get productId from document ID
                    val productData = document.toObject(ProductDataModel::class.java)
                    productData?.productId = productId
//                    Log.d("Debug", "getProductsInLimit: ${productData}")
                    productData
                }
                trySend(ResultState.Success(productLimitData))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

        awaitClose {
            close()
        }
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductDataModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(COLLECTION_PRODUCT).document(productId).get()
            .addOnSuccessListener{
                val product = it.toObject(ProductDataModel::class.java)
                trySend(ResultState.Success(product!!))
            }
            .addOnFailureListener{
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose{
            close()
        }
    }

    override fun getUserById(uid: String): Flow<ResultState<UserDataModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(COLLECTION_USERS).document(uid).get()
            .addOnSuccessListener{
                val user = it.toObject(UserDataModel::class.java)!!.apply {
                    this.uid = it.id
                }
                trySend(ResultState.Success(user))
            }
            .addOnFailureListener{
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose{
            close()
        }
    }

    override fun updateUserProfile(userDataModel: UserDataModel): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        firestore.collection(COLLECTION_USERS).document(firebaseAuth.uid.toString()).set(userDataModel)
            .addOnSuccessListener{
                trySend(ResultState.Success("User Profile Updated Successfully"))
            }
            .addOnFailureListener{
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose{
            close()
        }
    }

    override fun updateUserImage(imageUri: Uri): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        firebaseStorage.reference.child("userProfile/${System.currentTimeMillis()}+${firebaseAuth.currentUser?.uid}").putFile(imageUri)
            .addOnSuccessListener{
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    trySend(ResultState.Success(image.toString()))
                }
            }
            .addOnFailureListener{
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose{
            close()
        }
    }
}



