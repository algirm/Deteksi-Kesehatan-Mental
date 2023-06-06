package com.kuliah.deteksikesehatanmental

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                                    navController.navigate("depresi")
                                }) {
                                    Text(text = "Depresi")
                                }
                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Anxiety")
                                }
                            }
                        }

                        composable("depresi") {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TopBarWithBackButton {
                                    navController.navigateUp()
                                }
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(18.dp)
                                    ) {
                                        items(meditasiDepresi.size) {
                                            MeditasiCard(meditasi = meditasiDepresi[it], onClick = {})
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }
                                    }
                                }
                            }
                        }

                        composable("anxiety") {
                            
                        }
                        
                        composable("depresi/{id}") {

                        }
                        
                        composable("anxiety/{id}") {

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
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp).height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(24.dp),
        onClick = { onClick(meditasi.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = meditasi.name)
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
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
            imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clickable { onArrowBackClick() }
                .size(40.dp)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeteksiKesehatanMentalTheme {
        Greeting("Android")
    }
}