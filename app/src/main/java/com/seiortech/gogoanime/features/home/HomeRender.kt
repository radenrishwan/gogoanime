package com.seiortech.gogoanime.features.home

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil3.compose.AsyncImage
import com.seiortech.gogoanime.NavigationItem


@Composable
fun RenderOptionCard() {
  val optionList = listOf(
    Pair("Anime List", Icons.AutoMirrored.Filled.List),
    Pair("Genres", Icons.Default.NightsStay),
    Pair("Search", Icons.Default.Search),
    Pair("History", Icons.Default.History),
    Pair("Collections", Icons.Default.Collections),
  )

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    optionList.forEach { (title, icon) ->
      OptionCard(
        title = title,
        icon = icon,
        modifier = Modifier.weight(1f),
      )
    }
  }
}

data class SelectionPageCard(
  val title: String,
  val subtitle: String,
  val imgUrl: String,
  val onTap: () -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderSelectionPageCard(
  navController: NavController,
  addedSeries: List<RecentlyAddedSeries>,
  ongoingSeries: List<OngoingSeries>,
) {
  val sheetState = rememberModalBottomSheetState()
  var showAddedSeries by remember { mutableStateOf(false) }
  var showOngoingSeries by remember { mutableStateOf(false) }

  if (showAddedSeries) {
    RenderRecentlyAddedSeriesModalBottomSheet(
      addedSeries,
      sheetState,
      onDismissRequest = {
        showAddedSeries = false
      },
    )
  }

  if (showOngoingSeries) {
    RenderOngoingSeries(
      ongoingSeries,
      sheetState,
      onDismissRequest = {
        showOngoingSeries = false
      },
    )
  }


  val sections = listOf(
    SelectionPageCard(
      "New Season",
      "Discover the new season anime",
      "https://gogocdn.net/cover/seirei-gensouki-2-1727379332.png"
    ),

    SelectionPageCard(
      "Popular",
      "Don't miss the popular anime",
      "https://gogocdn.net/images/anime/naruto_shippuden.jpg"
    ),
    SelectionPageCard(
      "Release Category",
      "See the anime by release category",
      "https://gogocdn.net/cover/arifureta-shokugyou-de-sekai-saikyou-season-3.png"
    ),
    SelectionPageCard(
      "Movies",
      "Take a look a Movies",
      "https://gogocdn.net/images/anime/A/11269.jpg"
    ),
    SelectionPageCard(
      "Recent Release",
      "See the recent release",
      "https://gogocdn.net/cover/rekishi-ni-nokoru-akujo-ni-naru-zo-1727376849.png",
      onTap = {
        showAddedSeries = true
      }
    ),
    SelectionPageCard(
      "Ongoing Series",
      "See the ongoing series",
      "https://gogocdn.net/cover/saikyou-onmyouji-no-isekai-tenseiki-dub.png",
      onTap = {
        showOngoingSeries = true
      }
    )
  )

  HorizontalMultiBrowseCarousel(
    state = rememberCarouselState { sections.count() },
    modifier = Modifier
      .width(412.dp)
      .height(221.dp),
    preferredItemWidth = 186.dp,
    itemSpacing = 8.dp,
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) { index ->
    val item = sections[index]

    Box(
      modifier = Modifier
        .width(186.dp)
        .height(221.dp)
        .clip(MaterialTheme.shapes.medium)
        .clickable { item.onTap() },
      contentAlignment = Alignment.BottomStart,
    ) {
      AsyncImage(
        model = item.imgUrl,
        contentDescription = item.title,
        modifier = Modifier
          .fillMaxSize()
          .clip(MaterialTheme.shapes.medium)
          .drawWithCache {
            onDrawWithContent {
              drawContent()
              drawRect(
                brush = Brush.verticalGradient(
                  0f to Color.Transparent,
                  0.5f to Color.Transparent,
                  1f to Color.Black,
                ),
                alpha = 0.5f,
              )
            }
          },
        contentScale = ContentScale.Crop,
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
          .padding(8.dp)
          .fillMaxWidth(),
      ) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleSmall.copy(
            color = Color.White,
          ),
        )
        Text(
          text = item.subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            color = Color.White,
          ),
          overflow = TextOverflow.Ellipsis,
          maxLines = 2,
        )
      }
    }
  }
}

@Composable
fun RenderRecentRelease(
  recentReleaseList: List<RecentRelease>,
  navController: NavController,
) {
  RenderHeaderTitle("Recent Release")
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(recentReleaseList) { item ->
      Box(
        modifier = Modifier
          .width(120.dp)
          .height(180.dp)
          .clip(MaterialTheme.shapes.medium)
          .clickable {
            navController.navigate(
              NavigationItem.Episode.route + "?slug=${item.episodeSlug}",
              navOptions = NavOptions
                .Builder()
                .setRestoreState(true)
                .build(),
            )
          },
        contentAlignment = Alignment.BottomStart,
      ) {
        AnimeCard(
          imgUrl = item.img,
          title = item.title,
          episode = item.eps,
        )
      }
    }
  }
}

@Composable
fun RenderPopularOngoingUpdate(
  itemList: List<PopularOngoingUpdate>,
  navController: NavController,
) {
  RenderHeaderTitle("Popular Ongoing Update")
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(itemList) { item ->
      Box(
        modifier = Modifier
          .clickable {
            navController.navigate(
              NavigationItem.Detail.route + "?slug=${item.detailSlug}&title=${item.title}",
              navOptions = NavOptions
                .Builder()
                .setRestoreState(true)
                .build(),
            )
          },
        contentAlignment = Alignment.BottomStart,
      ) {
        AnimeCard(
          imgUrl = item.img,
          title = item.title,
          episode = item.episode,
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderRecentlyAddedSeriesModalBottomSheet(
  addedSeries: List<RecentlyAddedSeries>,
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp, 16.dp, 0.dp, 16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Text("Recently Added Series", style = MaterialTheme.typography.titleMedium)
      var currentLetter = ""

      addedSeries.sortedBy { it.title }.forEach {
        if (it.title.first().toString() != currentLetter) {
          currentLetter = it.title.first().toString()
          Text(currentLetter, style = MaterialTheme.typography.titleMedium)
        }

        Text(it.title, style = MaterialTheme.typography.bodyMedium)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderOngoingSeries(
  ongoingSeries: List<OngoingSeries>,
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp, 16.dp, 0.dp, 16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Text("Recently Added Series", style = MaterialTheme.typography.titleMedium)
      var currentLetter = ""

      ongoingSeries.sortedBy { it.title }.forEach {
        if (it.title.first().toString() != currentLetter) {
          currentLetter = it.title.first().toString()
          Text(currentLetter, style = MaterialTheme.typography.titleMedium)
        }

        Text(it.title, style = MaterialTheme.typography.bodyMedium)
      }
    }
  }
}

@Composable
fun RenderHeaderTitle(title: String, withViewAll: Boolean = true, onTap: () -> Unit = {}) {
  Row(
    modifier = Modifier
      .padding(0.dp, 8.dp)
      .fillMaxWidth()
      .clickable {
        onTap()
      },
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleSmall,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
    )
    if (withViewAll) {
      Text(
        text = "View All",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
      )
    }
  }
}