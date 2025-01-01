package com.example.myshopping.presentation

import android.R.attr.data
import android.R.attr.password
import android.R.attr.phoneNumber
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshopping.R
import com.example.myshopping.domain.models.UserDataModel
import com.example.myshopping.presentation.components.CustomCircularProgressIndicator
import com.example.myshopping.presentation.navigation.Routes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun SignUpScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController) {

    val state = viewModel.registerUserState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current // Store the context for Toast

    // Use LaunchedEffect to handle side effects
    LaunchedEffect(state.value) {
        state.value.errorMessage?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        state.value.userData?.let {
            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.HomeScreen) {
                popUpTo("signup_screen") { inclusive = true }
            }
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
            value = firstName,
            onValueChange = {
                firstName = it
            },
            label = { Text(text = "First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = {
                lastName = it
            },
            label = { Text(text = "Last Name") },
            modifier = Modifier.fillMaxWidth()
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
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            label = { Text(text = "Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = { Text(text = "Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword
        )

        Spacer(modifier = Modifier.size(16.dp))
        if (state.value.isLoading) {
            CustomCircularProgressIndicator()
        }else {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val data = UserDataModel(firstName, lastName, email, password, phoneNumber)
                    viewModel.registerUser(data)
                },
                enabled = firstName.isNotEmpty()
                        && lastName.isNotEmpty()
                        && email.isNotEmpty()
                        && password.isNotEmpty()
                        && confirmPassword.isNotEmpty()
                        && password == confirmPassword
            ) { Text(text = "Sign Up") }
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = "Already have an account? Sign In")
            }
        }
    }
}


//@Preview
//@Composable
//fun PreviewSsignUpScreen() {
//    SsignUpScreen(navController = rememberNavController())
//}