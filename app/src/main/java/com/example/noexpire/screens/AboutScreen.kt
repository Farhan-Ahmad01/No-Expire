package com.example.noexpire.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun AboutScreen(navController: NavController) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scaffoldState: ScaffoldState = rememberScaffoldState()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "About",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(android.graphics.Color.rgb(77, 26, 24)),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home")
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back button")
                    }
                }
                )
        }
    ) {paddingValues ->
        val scrollState = rememberScrollState()
        var expanded by remember { mutableStateOf(false) }
        Column(modifier = Modifier.background(Color(android.graphics.Color.rgb(242, 224, 220)))) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(4.dp),
//                    .background(Color(android.graphics.Color.rgb(242, 224, 220))),
                horizontalAlignment = Alignment.Start,  // Align content to the start
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Welcome to No Expire, an app designed to help you keep track of your pantry items and their expiration dates. With No Expire, you can easily add products, and the app will notify you when an item is nearing its expiry date, so you never forget to use it in time.\n" +
                            "\n" +
                            "This app was developed as a part of my Android project. My name is Farhan, and I built No Expire using modern technologies like Jetpack Compose and the MVVM architecture. While it's not a professional-grade app, it offers a simple and functional solution for managing your pantry items.\n" +
                            "\n" +
                            "Thank you for using No Expire!",
                    fontSize = 16.sp,  // Increased font size for readability
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Justify  // Justify the text for even alignment
                )
            }
        }

    }
}