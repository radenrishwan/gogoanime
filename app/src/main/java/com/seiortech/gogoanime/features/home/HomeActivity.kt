package com.seiortech.gogoanime.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compose.GogoanimeTheme
import com.seiortech.gogoanime.global.LoadingComponent
import androidx.compose.material3.Text as Text1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(
  viewModel: HomeViewModel = viewModel(),
  navController: NavController,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        colors = topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
          Text1("Gogoanime")
        }
      )
    },
  ) { innerPadding ->
    val homeResponse = viewModel.homeResponse.collectAsState()
    val message = viewModel.message.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    LaunchedEffect(homeResponse) {
      viewModel.getHome()
    }

    if (isLoading.value) {
      LoadingComponent("Getting Home Data...")
    } else {
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        if (message.value != null) {
          Text1(message.value!!)
        }
        RenderSelectionPageCard(
          navController = navController,
          homeResponse.value?.data?.recentlyAddedSeries ?: emptyList(),
          homeResponse.value?.data?.ongoingSeries ?: emptyList(),
        )
        RenderOptionCard()
        homeResponse.value?.data?.let {
          RenderRecentRelease(
            it.recentRelease,
            navController = navController
          )
        }
        homeResponse.value?.data?.let {
          RenderPopularOngoingUpdate(
            it.popularOngoingUpdate,
            navController = navController
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  GogoanimeTheme {
//    HomeActivity()
  }
}