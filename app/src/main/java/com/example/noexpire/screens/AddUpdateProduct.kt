package com.example.noexpire.screens

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color.rgb
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.noexpire.data.ProductItem
import com.example.noexpire.viewModels.HomeViewModel
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddUpdateProduct(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val context = LocalContext.current
//    val imageUri = product?.imageId?.let { Uri.parse(it) }

    val products = homeViewModel.products.collectAsState()

    val name = remember {
        mutableStateOf("")
    }
    val imageId = remember {
        mutableStateOf("")
    }
    val qunatity = remember {
        mutableStateOf("1")
    }
    val chooseDialog = remember {
        mutableStateOf(false)
    }

    var remainingDays by remember { mutableStateOf(0) }
    var purchaseDate by remember { mutableStateOf(0L) }
    var expiryDate by remember { mutableStateOf(0L) }

    val cameraImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

//    val context = LocalContext.current

    // It remembers the launcher across recompositions, so you can launch the activity whenever needed.
    val cameraImamgeLauncher = rememberLauncherForActivityResult(
        // ActivityResultContracts.TakePicture() is a predefined contract provided by Android that allows you to capture an image using the device's camera.
        //It expects a URI as an input, which specifies where the image should be saved. In this code snippet, this URI is assumed to be the value stored in cameraImageUri
        contract = ActivityResultContracts.TakePicture()
    ) {
        // success: A boolean value that indicates whether the image capture was successful.
            success ->
        if(success) {
            Log.d("Image Capture", "Image URI: ${cameraImageUri.value}")
            cameraImageUri.value?.let {
                // send image to server
                Log.d("Image Capture", "Image successfully captured at $it")
                cameraImageUri.value = it
            }
        }
    }

    val imamgeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let {
            cameraImageUri.value = it
        }
    }

    // Purpose: This function creates a unique URI for storing the image captured by the camera.
    fun creatImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHMMSS", Locale.getDefault()).format(Date())
        val storageDir =
            ContextCompat.getExternalFilesDirs(
                context,
                Environment.DIRECTORY_PICTURES
            ).first() // fir ek temporary file banao is storage par aur us storage ki uri return kardo
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        )
    }


    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted->
        if(isGranted) {
            cameraImamgeLauncher.launch(creatImageUri(context))
        }
    }


    fun calculateRemainingDays(expiry: Long): Int {
        if(purchaseDate == expiryDate) {
            return 0
        } else {
            val remainingTimeInMillis = expiry - System.currentTimeMillis()
            return 1+ceil(TimeUnit.MILLISECONDS.toDays(remainingTimeInMillis).toDouble()).toInt()
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                        Text(
                            text = "Add New Product",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back button")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(rgb(77, 26, 24)),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )

            )
        }

    ) {paddingValues ->

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(rgb(242, 224, 220)))
            .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Name", fontSize = 24.sp, color = Color(rgb(77, 26, 24)), modifier = Modifier.padding(start = 6.dp))
                        TextField(
                            value = name.value,
                            onValueChange = {name.value  = it},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Enter product name")},
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color(rgb(77, 26, 24)),
                                unfocusedTextColor = Color(rgb(77, 26, 24)),
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                unfocusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(rgb(77, 26, 24))
                            ),
                            singleLine = true
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Purchase Date", fontSize = 24.sp, color = Color(rgb(77, 26, 24)), modifier = Modifier.padding(start = 6.dp))
                        TextField(
                            value = if (purchaseDate != 0L) DateFormat.getDateInstance().format(Date(purchaseDate)) else "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Choose purchase date")},
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color(rgb(77, 26, 24)),
                                unfocusedTextColor = Color(rgb(77, 26, 24)),
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                unfocusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(rgb(77, 26, 24))
                            ),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(
                                        context = context,
                                        initialDate = purchaseDate,
                                        onDateSelected = { selectedDate ->
                                            purchaseDate = selectedDate.toEpochDay() * 86400000L
                                        }
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Select Purchase date"
                                    )
                                }
                            }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Expire Date", fontSize = 24.sp, color = Color(rgb(77, 26, 24)), modifier = Modifier.padding(start = 6.dp))
                        TextField(
                            value = if (expiryDate != 0L) DateFormat.getDateInstance().format(Date(expiryDate)) else "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Choose date of expiration")},
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color(rgb(77, 26, 24)),
                                unfocusedTextColor = Color(rgb(77, 26, 24)),
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                unfocusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(rgb(77, 26, 24))
                            ),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(
                                        context = context,
                                        initialDate = expiryDate,
                                        onDateSelected = { selectedDate ->
                                            expiryDate = selectedDate.toEpochDay() * 86400000L
                                        }
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Select Expire date"
                                    )
                                }
                            }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Quantity", fontSize = 24.sp, color = Color(rgb(77, 26, 24)), modifier = Modifier.padding(start = 6.dp))
                        TextField(
                            value = qunatity.value,
                            onValueChange = {qunatity.value  = it},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Enter Quantity")},
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color(rgb(77, 26, 24)),
                                unfocusedTextColor = Color(rgb(77, 26, 24)),
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                unfocusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedTrailingIconColor = Color(rgb(77, 26, 24)),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(rgb(77, 26, 24))
                            ),
                            singleLine = true
                        )
                    }
                }


                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(170.dp)
                        .clip(RoundedCornerShape(8.dp))
                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(rgb(242, 242, 242)))
                            .clickable {
                                       chooseDialog.value = true
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (cameraImageUri.value != null) {
                            AsyncImage(
                                model = cameraImageUri.value,
                                contentDescription = "Captured image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
//                            Image(
//                                painter = rememberImagePainter(data = cameraImageUri.value),
//                                contentDescription = null,
//                                modifier = Modifier.size(128.dp)
//                            )
                        } else {
                            Text(text = "Upload Image")
                        }
                    }
                }

                Button(onClick = {
                    if(name.value != "") {
                        remainingDays = calculateRemainingDays(expiryDate)
                        homeViewModel.addProduct(
                            ProductItem(
                                name = name.value,
                                purchaseDate = purchaseDate,
                                expiryDate = expiryDate,
                                quantity = qunatity.value,
                                imageId = cameraImageUri.value.toString(),
                                remainingDays = remainingDays
                            )
                        )
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Please Enter valid information", Toast.LENGTH_SHORT).show()
                    }
                },
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(rgb(77, 26, 24))),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Add Product", color = Color.White, fontSize = 18.sp)
                }

            }
        }

    }

    if (chooseDialog.value) {
        contentSlectorDialog(onCameraSelected = {
            chooseDialog.value = false
            if(context.checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                val imageUri = creatImageUri(context) // Get the new image URI
                cameraImageUri.value = imageUri // Set the state to hold the URI
                cameraImamgeLauncher.launch(imageUri) // Launch the camera intent with the UR
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }, onGallerySelected = {chooseDialog.value = false
            imamgeLauncher.launch("image/*")
        })
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(
    context: Context,
    initialDate: Long,
    onDateSelected: (LocalDate) -> Unit
) {
    val initialCalendar = Calendar.getInstance().apply {
        if (initialDate != 0L) {
            timeInMillis = initialDate
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // When date is selected, update the state
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        },
        initialCalendar.get(Calendar.YEAR),
        initialCalendar.get(Calendar.MONTH),
        initialCalendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.show()
}

@Composable
fun contentSlectorDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit ) {
    AlertDialog(
        onDismissRequest = {  },
        confirmButton = {
            TextButton(onClick = onCameraSelected) {
                androidx.compose.material3.Text(text = "Camera", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onGallerySelected) {
                androidx.compose.material3.Text(text = "Galley", color = Color.Black)
            }
        },
        title = { androidx.compose.material3.Text(text = "Choose your option", color = Color.Black) },
        containerColor = Color(rgb(242, 224, 220)),

    )
}
