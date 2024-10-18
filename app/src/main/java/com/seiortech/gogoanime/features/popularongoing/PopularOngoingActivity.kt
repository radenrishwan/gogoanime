package com.seiortech.gogoanime.features.popularongoing

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil3.compose.AsyncImage
import com.seiortech.gogoanime.NavigationItem
import com.seiortech.gogoanime.ui.theme.IMG_PLACEHOLDER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularOngoingActivity(
        viewModel: PopularOngoingViewModel = viewModel(),
        navController: NavController
) {
  val popularOngoingResponse = viewModel.popularOngoingResponse.collectAsState()
  val message = viewModel.message.collectAsState()
  val isLoading = viewModel.isLoading.collectAsState()

  Scaffold(
          topBar = {
            Column {
              TopAppBar(
                      colors =
                              topAppBarColors(
                                      containerColor = MaterialTheme.colorScheme.primaryContainer,
                                      titleContentColor = MaterialTheme.colorScheme.primary,
                              ),
                      title = { Text("Gogoanime") },
                      navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                      }
              )

              // show linear progress indicator when loading
              if (isLoading.value) {
                LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                )
              }
            }
          },
  ) { innerPadding ->
    val currentPage = remember { mutableIntStateOf(1) }

    val listState = rememberLazyGridState()

    LaunchedEffect(popularOngoingResponse) { viewModel.getPopularOngoings(1) }

    if (message.value != null) {
      Log.d("PopularOngoingActivity", "PopularOngoingActivity: $message")
      Text(message.value!!)
    }

    LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
    ) {
      items(popularOngoingResponse.value?.data?.size ?: 0) { animeItem ->
        val anime = popularOngoingResponse.value?.data?.get(animeItem)

        Column(
                modifier =
                        Modifier.fillMaxWidth().height(220.dp).clickable {
                          navController.navigate(
                                  NavigationItem.Detail.route +
                                          "?slug=${anime?.detailSlug}&title=${anime?.title}",
                                  navOptions = NavOptions.Builder().setRestoreState(true).build(),
                          )
                        },
                verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
          AsyncImage(
                  model = anime?.img ?: IMG_PLACEHOLDER,
                  contentDescription = anime?.title ?: "",
                  modifier =
                          Modifier.fillMaxWidth().height(160.dp).clip(MaterialTheme.shapes.medium),
                  contentScale = ContentScale.Crop,
          )
          Column(
                  modifier = Modifier.fillMaxWidth().padding(2.dp, 0.dp),
          ) {
            Text(
                    text = anime?.title ?: "",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
            )
            Text(
                    text = "Episode " + (anime?.episode ?: "Unknown"),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style =
                            MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Gray,
                            ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
            )
          }
        }

        if (animeItem == popularOngoingResponse.value?.data?.size?.minus(1)) {
          // load more item
          LaunchedEffect(popularOngoingResponse) {
            currentPage.intValue++
            viewModel.loadMorePopularOngoings(currentPage.intValue)
          }
        }
      }
    }
  }
}
