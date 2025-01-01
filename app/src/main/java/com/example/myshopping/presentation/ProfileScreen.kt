package com.example.myshopping.presentation

import android.R.attr.maxWidth
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myshopping.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.myshopping.presentation.components.CustomCircularProgressIndicator
import com.example.myshopping.presentation.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Route
import com.example.myshopping.domain.models.UserDataModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenUI(
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    auth: FirebaseAuth
) {

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserById(uid = auth.currentUser?.uid.toString())
    }

    val profileScreenState = viewModel.profileScreenState.collectAsStateWithLifecycle()
    val updateScreenState = viewModel.updateScreenState.collectAsStateWithLifecycle()
    val userProfileImageState = viewModel.userProfileImageState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    var isEditing by remember { mutableStateOf(false) }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(profileScreenState.value.userData) {
        profileScreenState.value.userData?.let { userData ->
            Log.d("ProfileScreenUI", "User data fetched: $userData")
            firstName = userData.firstName.toString()
            lastName = userData.lastName.toString()
            email = userData.email.toString()
            phoneNumber = userData.phoneNumber.toString()
            address = userData.address.toString()
            imageUrl = userData.userImage.toString()
        }
    }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.updateUserProfileImage(uri)
                imageUri = uri
            }
        }

    when {
        updateScreenState.value.isLoading -> {
            CustomCircularProgressIndicator()
        }

        updateScreenState.value.errorMessage != null -> Toast.makeText(
            context,
            updateScreenState.value.errorMessage,
            Toast.LENGTH_SHORT
        ).show()

        updateScreenState.value.userData != null -> Toast.makeText(
            context,
            updateScreenState.value.userData,
            Toast.LENGTH_SHORT
        ).show()
    }

    when {
        userProfileImageState.value.isLoading -> { CustomCircularProgressIndicator()}
        userProfileImageState.value.errorMessage != null -> Toast.makeText(context, userProfileImageState.value.errorMessage, Toast.LENGTH_SHORT).show()
        userProfileImageState.value.userData != null -> {
            imageUrl = userProfileImageState.value.userData.toString()
            Log.d("profileImage", imageUrl)
        }
    }

    when {
        profileScreenState.value.isLoading -> { CustomCircularProgressIndicator()}
        profileScreenState.value.errorMessage != null -> {
            Text(text = profileScreenState.value.errorMessage!!)
        }
        profileScreenState.value.userData != null -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Profile", fontWeight = FontWeight.Bold) }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Start)
                    ) {
                        SubcomposeAsyncImage(
                            model = if (isEditing) imageUri else imageUrl,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    CustomCircularProgressIndicator()
                                }

                                is AsyncImagePainter.State.Error -> {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                }

                                else -> SubcomposeAsyncImageContent()
                            }
                        }
                        if (isEditing) {
                            IconButton(
                                onClick = {
                                    pickMedia.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Change Picture",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(16.dp))
                    Row {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text(text = "First Name") },
                            modifier = Modifier.weight(1f),
                            readOnly = if (isEditing) false else true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Magenta,
                                focusedTextColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text(text = "Last Name") },
                            modifier = Modifier.weight(1f),
                            readOnly = if (isEditing) false else true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Magenta,
                                focusedTextColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        readOnly = if (isEditing) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Magenta,
                            focusedTextColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text(text = "Phone Number") },
                        readOnly = if (isEditing) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Magenta,
                            focusedTextColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text(text = "Address") },
                        readOnly = if (isEditing) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Magenta,
                            focusedTextColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    OutlinedButton(
                        onClick = {
//                            showDialog = true
                            auth.signOut()
                            navController.navigate(Routes.LoginScreen)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color.Magenta)
                    ) {
                        Text(text = "Log Out")
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    if (isEditing == false) {
                        OutlinedButton(
                            onClick = {
                                isEditing = !isEditing
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = "Edit Profile")
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                val updateUserData = UserDataModel(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    phoneNumber = phoneNumber,
                                    address = address,
                                    userImage = imageUrl
                                )
                                viewModel.updateUserProfile(updateUserData)

                                isEditing = !isEditing
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = "Save Profile")
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun ProfileScreenUIPreview() {
//    ProfileScreenUI(navController = rememberNavController())
//}
