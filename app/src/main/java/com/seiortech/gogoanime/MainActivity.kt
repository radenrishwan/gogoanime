package com.seiortech.gogoanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.compose.GogoanimeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      GogoanimeTheme {
        Main()
      }
    }
  }
}

@Composable
fun Main() {
  val navController = rememberNavController()

  AppNavHost(
    navController = navController,
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  GogoanimeTheme {
    Main()
  }
}