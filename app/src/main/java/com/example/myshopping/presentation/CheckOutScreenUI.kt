package com.example.myshopping.presentation

import android.R.attr.singleLine
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myshopping.MainActivity
import com.example.myshopping.presentation.components.CustomCircularProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreenUI(
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavHostController,
    productId: String
) {
    val state = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val productData = state.value.productData

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = context as? MainActivity

    var email by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    val phoneNumber by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("Standard Free delivery over Rs.499") }

    LaunchedEffect(key1 = Unit){
        coroutineScope.launch(Dispatchers.IO){
            viewModel.getProductById(productId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Shipping", fontWeight = FontWeight.Bold)},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back_button")
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            state.value.isLoading -> {
                CustomCircularProgressIndicator()
            }

            state.value.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Sorry, unable to get information")
                }
            }

            state.value.productData == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Nothing to Show!!")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = productData!!.imageUrl,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                .size(80.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
//                        Column(
//                            modifier = Modifier.weight(2f)
//                        ) {
//                            Text(text = productData.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
//                            Text(text = "₹${productData.finalPrice}", style = MaterialTheme.typography.bodyLarge)
//                        }
                        Text(text = productData.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "₹${productData.finalPrice}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(text = "Contact Information", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {email = it},
                            modifier = Modifier.fillMaxWidth(),
                            label = {Text(text = "Email")},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Shipping Address",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = country,
                            onValueChange = {country = it},
                            modifier = Modifier.fillMaxWidth(),
                            label = {Text(text = "Country")},
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = {firstName = it},
                                modifier = Modifier.weight(1f),
                                label = {Text(text = "First Name")},
                                singleLine = true,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = {lastName = it},
                                modifier = Modifier.weight(1f),
                                label = {Text(text = "Last Name")},
                                singleLine = true
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = {address = it},
                            modifier = Modifier.fillMaxWidth(),
                            label = {Text(text = "Address")},
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {city = it},
                                modifier = Modifier.weight(1f),
                                label = {Text(text = "City")},
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = postalCode,
                                onValueChange = {postalCode = it},
                                modifier = Modifier.weight(1f),
                                label = {Text(text = "Postal Code")},
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            Text(text = "Shipping Method", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedMethod == "Standard Free delivery over Rs.499",
                                    onClick = {
                                        selectedMethod = "Standard Free delivery over Rs.499"
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Standard Free delivery over Rs.499")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedMethod == "Cash On Delivery Rs.50",
                                    onClick = {
                                        selectedMethod = "Cash On Delivery Rs.50"
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Cash On Delivery Rs.50")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                activity?.startPayment(
                                    amount = productData!!.finalPrice.toInt(),
                                    name = productData.name
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continue to Shipping")
                        }
                    }
                }
            }
        }
    }
}