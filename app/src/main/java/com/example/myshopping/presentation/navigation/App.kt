package com.example.myshopping.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.myshopping.presentation.HomeScreenUI
import com.example.myshopping.presentation.LoginScreen
import com.example.myshopping.presentation.SignUpScreen
import com.example.myshopping.presentation.navigation.Routes.SignupScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.myshopping.presentation.CartScreenUI
import com.example.myshopping.presentation.CheckOutScreenUI
import com.example.myshopping.presentation.EachProductDetailsScreen
import com.example.myshopping.presentation.ProfileScreenUI
import com.example.myshopping.presentation.WishListScreenUI

@Composable
fun App(firebaseAuth: FirebaseAuth) {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavigationItem("Favorite", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavigationItem("Cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavigationItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    var selectedItemIndex by remember { mutableStateOf(0) }
    val currentDestinationAsState = navController.currentBackStackEntryAsState()
    val currentDestination = currentDestinationAsState.value?.destination?.route
    val shouldShowBottomBar = remember { mutableStateOf(false) }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when(currentDestination){
            Routes.LoginScreen::class.qualifiedName, Routes.SignupScreen::class.qualifiedName -> false
            else -> true
        }
    }

    var startScreen = if (firebaseAuth.currentUser == null){
        SubNavigation.LoginSignUpScreen
    }else{
        SubNavigation.MainHomeScreen
    }

    Scaffold (
        bottomBar = {
            if (shouldShowBottomBar.value) {
                NavigationBar {
                    items.forEachIndexed { index, bottomNavigationItem ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                when (selectedItemIndex) {
                                    0 -> navController.navigate(Routes.HomeScreen)
                                    1 -> navController.navigate(Routes.WishListScreen)
                                    2 -> navController.navigate(Routes.CartScreen)
                                    3 -> navController.navigate(Routes.ProfileScreen)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon,
                                    contentDescription = bottomNavigationItem.title
                                )
                            }
                        )
                    }
                }
            }
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                bottom = if (shouldShowBottomBar.value) innerPadding.calculateBottomPadding() else 0.dp
            )
        ){
            NavHost(navController, startDestination = startScreen){

                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.LoginScreen){
                    composable<Routes.LoginScreen>{
                        LoginScreen(navController = navController)
                    }
                    composable<Routes.SignupScreen>{
                        SignUpScreen(navController = navController)
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen){
                    composable<Routes.HomeScreen>{
                        HomeScreenUI(navController = navController)
                    }
                    composable<Routes.WishListScreen>{
                        WishListScreenUI(navController = navController)
                    }
                    composable<Routes.CartScreen>{
                        CartScreenUI(navController = navController)
                    }
                    composable<Routes.ProfileScreen>{
                        ProfileScreenUI(auth = FirebaseAuth.getInstance(),navController = navController)
                    }
                }

                composable<Routes.EachProductDetailsScreen>{
                    val data = it.toRoute<Routes.EachProductDetailsScreen>()
                    EachProductDetailsScreen(
                        navController = navController,
                        productId = data.productId
                    )
                }
                composable<Routes.CheckoutScreen>{
                    val data = it.toRoute<Routes.CheckoutScreen>()
                    CheckOutScreenUI(
                        navController = navController,
                        productId = data.productId
                    )
                }
            }
        }
    }

}



data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)