package com.seiortech.gogoanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.GogoanimeTheme
import com.seiortech.gogoanime.features.detail.DetailActivity
import com.seiortech.gogoanime.features.home.HomeActivity

public const val BASE_URL = "https://gogoanime-api-500987716325.asia-southeast2.run.app/api/"

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

enum class Screen {
  HOME,
  DETAIL,
}

sealed class NavigationItem(val route: String) {
  object Home : NavigationItem(Screen.HOME.name)
  object Detail : NavigationItem(Screen.DETAIL.name)
}

@Composable
fun AppNavHost(
  navController: NavHostController,
) {
  NavHost(
    navController = navController,
    startDestination = NavigationItem.Home.route,
  ) {
    composable(
      NavigationItem.Home.route,
      enterTransition = {
        slideIntoContainer(
          AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(300)
        )
      },
      exitTransition = {
        slideOutOfContainer(
          AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(300)
        )
      },
    ) {
      HomeActivity(navController = navController)
    }
    composable(
      NavigationItem.Detail.route + "?slug={slug}&title={title}",
      arguments = listOf(
        navArgument("slug") { type = NavType.StringType },
        navArgument("title") { type = NavType.StringType },
      ),
      enterTransition = {
        slideIntoContainer(
          AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(300)
        )
      },
      exitTransition = {
        slideOutOfContainer(
          AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(300)
        )
      },
    ) { backStackEntry ->
      // get the slug and title from the arguments
      val slug = backStackEntry.arguments?.getString("slug")
      val title = backStackEntry.arguments?.getString("title")

      DetailActivity(
        navController = navController,
        slug = slug!!,
        title = title!!,
      )
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