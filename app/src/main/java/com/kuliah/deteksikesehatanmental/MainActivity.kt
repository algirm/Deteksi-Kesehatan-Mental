package com.kuliah.deteksikesehatanmental

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kuliah.deteksikesehatanmental.ui.theme.DeteksiKesehatanMentalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeteksiKesehatanMentalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val sharedPref = LocalContext.current.getSharedPreferences(
                        "app_pref",
                        Context.MODE_PRIVATE
                    )
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Anda mengidap penyakit mental apa?")
                                Spacer(modifier = Modifier.height(40.dp))
                                Button(onClick = {
                                    navController.navigate("list/depresi")
                                }) {
                                    Text(text = "Depresi")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = {
                                    navController.navigate("list/anxiety")
                                }) {
                                    Text(text = "Anxiety")
                                }
                            }
                        }

                        composable(
                            route = "list/{meditasi}",
                            arguments = listOf(
                                navArgument("meditasi") {
                                    type = NavType.StringType
                                }
                            )
                        ) { navBackStackEntry ->
                            val type =
                                navBackStackEntry.arguments?.getString("meditasi") ?: "depresi"
                            val meditasiList =
                                if (type == "depresi") meditasiDepresi else meditasiAnxiety
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TopBarWithBackButton {
                                    navController.navigateUp()
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        item {
                                            Text(
                                                text = "Meditasi Untuk ${type.replaceFirstChar { it.uppercaseChar() }}",
                                                fontSize = 20.sp,
                                                modifier = Modifier.padding(
                                                    vertical = 24.dp,
                                                    horizontal = 18.dp
                                                )
                                            )
                                        }
                                        items(meditasiList.size) {
                                            var isFavorited: Boolean by remember {
                                                mutableStateOf(
                                                    sharedPref.getBoolean(
                                                        meditasiList[it].id.toString(),
                                                        false
                                                    )
                                                )
                                            }
                                            MeditasiCard(
                                                meditasi = meditasiList[it],
                                                isFavorited = isFavorited,
                                                onClick = { id ->
                                                    navController.navigate("meditasi/$id")
                                                },
                                                onFavoriteClick = { id ->
                                                    sharedPref.edit {
                                                        putBoolean(id.toString(), !isFavorited)
                                                    }
                                                    isFavorited = !isFavorited
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }
                                    }
                                }
                            }
                        }

                        composable(
                            route = "meditasi/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                }
                            )
                        ) { navBackStackEntry ->
                            val id = (navBackStackEntry.arguments?.getInt("id") ?: 0) - 1
                            val meditasi = if (id < 4) meditasiDepresi[id] else meditasiAnxiety[id - meditasiAnxiety.size]
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TopBarWithBackButton {
                                    navController.navigateUp()
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 18.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = meditasi.name,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(vertical = 24.dp)
                                    )
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        shape = RoundedCornerShape(24.dp),
                                    ) {
                                        Text(
                                            text = meditasi.description,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditasiCard(
    meditasi: Meditasi,
    isFavorited: Boolean,
    onClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(2.dp),
        onClick = { onClick(meditasi.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = meditasi.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.9f),
                    minLines = 1
                )
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .defaultMinSize(24.dp, 24.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onFavoriteClick(meditasi.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = meditasi.description,
                    fontSize = 11.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(0.9f)
                )
                Spacer(modifier = Modifier.weight(0.1f))
            }
        }

    }
}

@Composable
fun TopBarWithBackButton(
    onArrowBackClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clickable { onArrowBackClick() }
                .size(40.dp)
        )
    }
}