package com.example.myshopping.presentation.navigation

import kotlinx.serialization.Serializable


sealed class SubNavigation {

    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object LoginSignUpScreen : SubNavigation()
}

sealed class Routes {

    @Serializable
    object LoginScreen

    @Serializable
    object SignupScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishListScreen

    @Serializable
    object CartScreen

    @Serializable
    data class CheckoutScreen(
        val productId: String
    )

    @Serializable
    object PaymentScreen

    @Serializable
    object SeeAllProductScreen

    @Serializable
    data class EachProductDetailsScreen(
        val productId: String
    )

    @Serializable
    object AllCategoryScreen

    @Serializable
    object EachCategoryItemsScreen
}