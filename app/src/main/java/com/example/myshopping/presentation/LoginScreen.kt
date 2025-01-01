package com.example.myshopping.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshopping.R
import com.example.myshopping.presentation.components.CustomCircularProgressIndicator
import com.example.myshopping.presentation.navigation.Routes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun LoginScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController) {

    val loginState = viewModel.loginUserState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(loginState.value) {
        loginState.value.errorMessage?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        loginState.value.userData?.let { userDate ->
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.HomeScreen)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.cartlogo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
//                .background(Color.White)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(16.dp))
        if (loginState.value.isLoading){
            CustomCircularProgressIndicator()
        }else {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.loginWithEmailPassword(
                        email = email,
                        password = password
                    )
                },
                enabled = email.isNotEmpty() && password.isNotEmpty()
            ) { Text(text = "Login") }
            TextButton(onClick = { navController.navigate(Routes.SignupScreen) }) {
                Text(text = "Don't have an account? Sign Up")
            }
        }
    }

}


//@Preview(showSystemUi = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen(navController = rememberNavController())
//}