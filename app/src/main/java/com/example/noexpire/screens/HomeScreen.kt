package com.example.noexpire.screens

import android.annotation.SuppressLint
import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.noexpire.R
import com.example.noexpire.data.ProductItem
import com.example.noexpire.viewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    var topExpanded by remember { mutableStateOf(false) }
    val scope: CoroutineScope = rememberCoroutineScope()

    val products = homeViewModel.products.collectAsState()

    var searchItem by remember {
        mutableStateOf("")
    }
    val grayColor = Color(rgb(48, 55, 56))
    val greenShade = Color(rgb(69, 181, 144))
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pantry items",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                                 IconButton(onClick = {
                                        navController.navigate("about")
                                 }) {
                                     Icon(imageVector = Icons.Default.Info, contentDescription = "back button")
                                 }
                },
                actions = { 
                    IconButton(onClick = { topExpanded = true}) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "list")
                    }

                    DropdownMenu(
                        expanded = topExpanded,
                        onDismissRequest = { topExpanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                          Icon(
                                              imageVector = Icons.Default.Info,
                                              contentDescription = null,
                                              tint = Color.Black
                                          )
                            },
                            text = { Text("Help", color = Color.Black) },
                            onClick = {
                                
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                          Icon(
                                              imageVector = Icons.Default.Settings,
                                              contentDescription = null,
                                              tint = Color.Black
                                          )
                            },
                            text = { Text("Setting", color = Color.Black) },
                            onClick = {}
                        )
                    }
                    
                }
                
                ,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(rgb(77, 26, 24)),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },

      bottomBar = {
            BottomAppBar(
                containerColor = Color(rgb(77, 26, 24)),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(
                        onClick = {
                                  navController.navigate("addUpdate")
                        },
                        containerColor = Color.White,
                        contentColor = Color(rgb(77, 26, 24)),
                        shape = RoundedCornerShape(20.dp),
                        elevation = FloatingActionButtonDefaults.elevation(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add",
                        )
                    }
                }
            }
        }

    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(rgb(242, 224, 220)))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchItem,
                        onValueChange = {searchItem = it},
                        shape = RoundedCornerShape(25.dp),
                        placeholder = { Text(text = "Search...", color = Color(rgb(77, 26, 24)))},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
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
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search",
                                tint = Color(rgb(77, 26, 24))
                            )
                        },
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 5.dp)
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Red),
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                        Text(text = "Expired", color = Color.Black)
                    }


                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color(255, 243, 33)),
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                            Text(text = "Expiring Soon", color = Color.Black)
                        }



                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.Green),
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                            Text(text = "Fresh", color = Color.Black)
                        }
                    }


                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn {
                        items(products.value) { product ->
                            productCard(productItem = product, homeViewModel)
                        }
                    }
                }

            }
        }
    }

}

@Composable
fun productCard(productItem: ProductItem, homeViewModel: HomeViewModel) {

    var expanded by remember { mutableStateOf(false) }

    val stateColor = if(productItem.remainingDays >= 10) { Color.Green}
    else if(productItem.remainingDays <10 && productItem.remainingDays >0 ) {Color(255, 243, 33)} else {Color.Red}

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(110.dp)
        .padding(bottom = 6.dp)) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)) {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .width(95.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                        if(productItem.imageId == "null") {
                            Image(
                                painter = painterResource(id = R.drawable.notimage),
                                contentDescription = null,
                                modifier = Modifier.clickable {  }
                            )
                        } else {
                            AsyncImage(
                                model = productItem.imageId?.toUri(),
                                contentDescription = "image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(15.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }


                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                        productItem.name?.let {
                            Text(
                                text = it,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(rgb(77, 26, 24))
//                                color = stateColor
                            )
                        }
                        Spacer(modifier = Modifier.padding(start = 5.dp, end = 3.dp))
                        Text(text = "x${productItem.quantity}", color = Color.Black)
                    }

                    Text(
                        text = "Purchased: ${DateFormat.getDateInstance().format(Date(productItem.purchaseDate))}",
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Expires On: ${DateFormat.getDateInstance().format(Date(productItem.expiryDate))}",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }

                Column(modifier = Modifier
                    .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete", color = Color.Black) },
                                onClick = {
                                    homeViewModel.deleteProduct(productItem)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Edit", color = Color.Black) },
                                onClick = {expanded = false}
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(color = stateColor),
                    )

                    Text(
                        text = "${productItem.remainingDays}d",
                        fontWeight = FontWeight.Bold,
//                        color = Color(rgb(77, 26, 24))
                        color = Color.Black
                    )
                }

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun prefasffda() {
    productCard(productItem = ProductItem(2, "Farhan", 4, 6), homeViewModel = HomeViewModel())
}













