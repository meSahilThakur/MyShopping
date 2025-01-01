package com.example.myshopping.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshopping.common.ResultState
import com.example.myshopping.domain.models.Category
import com.example.myshopping.domain.models.ProductDataModel
import com.example.myshopping.domain.models.UserDataModel
import com.example.myshopping.domain.useCase.GetAllCategoriesUseCase
import com.example.myshopping.domain.useCase.GetAllProductsUseCase
import com.example.myshopping.domain.useCase.GetCategoriesInLimitUseCase
import com.example.myshopping.domain.useCase.GetProductByIdUseCase
import com.example.myshopping.domain.useCase.GetProductsInLimitUseCase
import com.example.myshopping.domain.useCase.GetUserByIdUseCase
import com.example.myshopping.domain.useCase.LoginWithEmailPasswordUseCase
import com.example.myshopping.domain.useCase.RegisterUserWithEmailPasswordUseCase
import com.example.myshopping.domain.useCase.UpdateUserDataUseCase
import com.example.myshopping.domain.useCase.UpdateUserImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val registerUserWithEmailPassword: RegisterUserWithEmailPasswordUseCase,
    private val loginUserWithEmailPassword: LoginWithEmailPasswordUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getCategoriesInLimitUseCase: GetCategoriesInLimitUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductsInLimitUseCase: GetProductsInLimitUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase
): ViewModel() {

    private val _registerUserState = MutableStateFlow(RegisterUserState())
    val registerUserState = _registerUserState.asStateFlow()

    private val _loginUserState = MutableStateFlow(LoginUserState())
    val loginUserState = _loginUserState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()


    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()

    private val _userProfileImageState = MutableStateFlow(UploadUserProfileImageState())
    val userProfileImageState = _userProfileImageState.asStateFlow()

    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState = _updateScreenState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()




    fun updateUserProfile(userDataModel: UserDataModel){
        viewModelScope.launch(Dispatchers.IO){
            updateUserDataUseCase.updateUserDataUseCase(userDataModel).collectLatest{
                when(it){
                    is ResultState.Loading -> {
                        _updateScreenState.value = UpdateScreenState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateScreenState.value = UpdateScreenState(userData = it.data)
                    }
                    is ResultState.Error -> {
                        _updateScreenState.value = UpdateScreenState(errorMessage = it.error)
                    }
                }
            }
        }
    }
    fun updateUserProfileImage(imageUri:Uri){
        viewModelScope.launch(Dispatchers.IO){
            updateUserImageUseCase.updateUserImageUseCase(imageUri).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _userProfileImageState.value = UploadUserProfileImageState(isLoading = true)
                    }
                    is ResultState.Success<*> -> {
                        _userProfileImageState.value = UploadUserProfileImageState(userData = it.data.toString())
                    }
                    is ResultState.Error -> {
                        _userProfileImageState.value = UploadUserProfileImageState(errorMessage = it.error)
                    }
                }
            }
        }
    }

    fun loginWithEmailPassword(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO){
            loginUserWithEmailPassword.loginWithEmailPasswordUseCase(email, password).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _loginUserState.value = LoginUserState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _loginUserState.value = LoginUserState(userData = it.data)
                    }
                    is ResultState.Error -> {
                        _loginUserState.value = LoginUserState(errorMessage = it.error)
                    }
                }
            }
        }
    }

    fun registerUser(userDataModel: UserDataModel){
        viewModelScope.launch(Dispatchers.IO){
            registerUserWithEmailPassword.registerUserWithEmailPasswordUseCase(userDataModel).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _registerUserState.value = RegisterUserState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _registerUserState.value = RegisterUserState(userData = it.data)
                    }
                    is ResultState.Error -> {
                        _registerUserState.value = RegisterUserState(errorMessage = it.error)
                    }
                }
            }
        }
    }

    fun getUserById(uid: String){
        viewModelScope.launch(Dispatchers.IO){
            getUserByIdUseCase.getUserByIdUseCase(uid).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _profileScreenState.value = ProfileScreenState(isLoading = true)
                    }
                    is ResultState.Error -> {
                        _profileScreenState.value = ProfileScreenState(errorMessage = it.error)
                    }
                    is ResultState.Success -> {
                        _profileScreenState.value = ProfileScreenState(userData = it.data)
                    }
                }
            }
        }
    }

    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO){
            combine(
                getCategoriesInLimitUseCase.getCategoriesInLimitUseCase(),
                getProductsInLimitUseCase.getProductsInLimitUseCase()
            ){ categoriesResult, productsResult ->
                when{
                    categoriesResult is ResultState.Success && productsResult is ResultState.Success -> {
                        HomeScreenState(
                            isLoading = false,
                            categoriesData = categoriesResult.data,
                            productsData = productsResult.data
                        )
                    }
                    categoriesResult is ResultState.Error -> {
                        HomeScreenState(
                            isLoading = false,
                            error = categoriesResult.error
                        )
                    }
                    productsResult is ResultState.Error -> {
                        HomeScreenState(
                            isLoading = false,
                            error = productsResult.error
                        )
                    }
                    else -> {
                        HomeScreenState(isLoading = true)
                    }
                }
            }.collect{
                state -> _homeScreenState.value = state
            }
        }
    }


    fun getAllCategories(){
        viewModelScope.launch{
            getAllCategoriesUseCase.getAllCategoriesUseCase().collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getAllCategoriesState.value = GetAllCategoriesState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getAllCategoriesState.value = GetAllCategoriesState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getAllCategoriesState.value = GetAllCategoriesState(error = it.error)
                    }
                }
            }
        }
    }


    fun getAllProducts(){
        viewModelScope.launch{
            getAllProductsUseCase.getAllProductsUseCase().collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getAllProductsState.value = GetAllProductsState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getAllProductsState.value = GetAllProductsState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getAllProductsState.value = GetAllProductsState(error = it.error)
                    }
                }
            }
        }
    }

    fun getProductById(productId: String){
        viewModelScope.launch(Dispatchers.IO){
            getProductByIdUseCase.getProductByIdUseCase(productId).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getProductByIdState.value = GetProductByIdState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getProductByIdState.value = GetProductByIdState(productData = it.data)
                    }
                    is ResultState.Error -> {
                        _getProductByIdState.value = GetProductByIdState(errorMessage = it.error)
                    }
                }
            }
        }
    }
}


data class RegisterUserState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)
data class LoginUserState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)


data class HomeScreenState(
    val isLoading: Boolean = false,
    val categoriesData: List<Category>? = null,
    val productsData: List<ProductDataModel>? = null,
    val error: String? = null
)


data class GetAllCategoriesState(
    val isLoading: Boolean = false,
    val data: List<Category?> = emptyList(),
    val error: String = ""
)
data class GetAllProductsState(
    val isLoading: Boolean = false,
    val data: List<ProductDataModel?> = emptyList(),
    val error: String = ""
)


data class ProfileScreenState(
    val isLoading: Boolean = false,
    val userData: UserDataModel? = null,
    val errorMessage: String? = null
)
data class UploadUserProfileImageState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)
data class UpdateScreenState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class GetProductByIdState(
    val isLoading: Boolean = false,
    val productData: ProductDataModel? = null,
    val errorMessage: String? = null
)

