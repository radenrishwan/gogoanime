package com.seiortech.gogoanime

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seiortech.gogoanime.features.detail.DetailActivity
import com.seiortech.gogoanime.features.episode.EpisodeActivity
import com.seiortech.gogoanime.features.home.HomeActivity


enum class Screen {
  HOME,
  DETAIL,
  EPISODE,
}

sealed class NavigationItem(val route: String) {
  data object Home : NavigationItem(Screen.HOME.name)
  data object Detail : NavigationItem(Screen.DETAIL.name)
  data object Episode : NavigationItem(Screen.EPISODE.name)
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
    composable(
      NavigationItem.Episode.route + "?slug={slug}",
      arguments = listOf(
        navArgument("slug") { type = NavType.StringType },
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
      // get the slug from the arguments
      val slug = backStackEntry.arguments?.getString("slug")

      EpisodeActivity(episodeSlug = slug.orEmpty(), navController = navController)
    }
  }
}