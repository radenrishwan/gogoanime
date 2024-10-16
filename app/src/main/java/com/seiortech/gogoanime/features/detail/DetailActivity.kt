package com.seiortech.gogoanime.features.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.compose.GogoanimeTheme
import com.seiortech.gogoanime.global.LoadingComponent
import com.seiortech.gogoanime.ui.theme.IMG_PLACEHOLDER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailActivity(
  title: String,
  slug: String,
  navController: NavController,
  viewModel: DetailViewModel = viewModel(),
) {

  Scaffold(
    topBar = {
      TopAppBar(
        colors = topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
          Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {
          IconButton(
            onClick = { navController.popBackStack() }
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back"
            )
          }
        }
      )
    },
  ) { innerPadding ->
    val detailResponse = viewModel.detailResponse.collectAsState()
    val message = viewModel.message.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    LaunchedEffect(detailResponse) {
      viewModel.getDetail(slug)
    }

    if (isLoading.value) {
      LoadingComponent("Getting Detail Data...")
    } else {
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          AsyncImage(
            model = detailResponse.value?.data?.img ?: IMG_PLACEHOLDER,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .fillMaxHeight()
              .weight(1f)
              .clip(MaterialTheme.shapes.medium),
          )
          Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Text(
              text = title,
              style = MaterialTheme.typography.titleSmall,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Text(
              text = detailResponse.value?.data?.otherTitles?.joinToString(", ") ?: "Unknown",
              style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Text(
              text =
              ("Genre: " + (detailResponse.value?.data?.genres?.joinToString(", ") ?: "Unknown")),
              style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
              ),
            )
            Text(
              text =
              ("Released: " + (detailResponse.value?.data?.details?.released ?: "Unknown")),
              style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
              ),
            )
            Card(
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
              ),
              elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp,
              ),
            ) {
              Text(
                text = detailResponse.value?.data?.details?.status ?: "Unknown",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = Color.White,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(6.dp, 4.dp),
              )
            }
          }
        }

        if (message.value != null) {
          Log.e("DetailActivity", message.value!!)
          Text("Something went wrong, please try again later.")
        }

        Text(
          text = "Plot Summary",
          style = MaterialTheme.typography.titleSmall,
        )
        Text(
          text = detailResponse.value?.data?.details?.plotSummary ?: "Unknown",
          style = MaterialTheme.typography.bodySmall,
        )

        HorizontalDivider()

        detailResponse.value?.data?.episodes?.reversed().orEmpty().forEach {
          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            elevation = CardDefaults.cardElevation(
              defaultElevation = 1.dp,
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                navController.navigate("episode?slug=${it.episodeSlug}")
              }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "Episode ${it.title}",
                style = MaterialTheme.typography.titleSmall,
              )
              Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
              )
            }
          }
        }
      }
    }
  }
}

@Composable
@Preview
fun DetailPreview() {
  GogoanimeTheme {
//    DetailActivity("One Piece")
  }
}