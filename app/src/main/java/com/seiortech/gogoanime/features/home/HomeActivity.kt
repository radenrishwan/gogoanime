package com.seiortech.gogoanime.features.home

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.seiortech.gogoanime.ui.theme.GogoanimeTheme
import androidx.compose.material3.Text as Text1


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
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderSelectionPageCard() {
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
        .clip(MaterialTheme.shapes.medium),
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
        Text1(
          text = item.title,
          style = MaterialTheme.typography.titleSmall,
        )
        Text1(
          text = item.subtitle,
          style = MaterialTheme.typography.bodySmall,
          overflow = TextOverflow.Ellipsis,
          maxLines = 2,
        )
      }
    }
  }
}

@Composable
fun RenderRecentRelease(
  recentReleaseList: List<RecentRelease>
) {
  RenderHeaderTitle("Recent Release")
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(recentReleaseList) { item ->
      AnimeCard(
        imgUrl = item.img,
        title = item.title,
        episode = item.eps,
      )
    }
  }
}

@Composable
fun RenderPopularOngoingUpdate(
  itemList: List<PopularOngoingUpdate>
) {
  RenderHeaderTitle("Popular Ongoing Update")
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(itemList) { item ->
      AnimeCard(
        imgUrl = item.img,
        title = item.title,
        episode = item.episode,
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderRecentlyAddedSeries(
  addedSeries: List<RecentlyAddedSeries>,
) {
  val sheetState = rememberModalBottomSheetState()
  var showBottomSheet by remember { mutableStateOf(false) }

  if (showBottomSheet) {
    ModalBottomSheet(
      sheetState = sheetState,
      onDismissRequest = {
        showBottomSheet = false
      },
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp, 16.dp, 0.dp, 16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text1("Recently Added Series", style = MaterialTheme.typography.titleMedium)
        var currentLetter = ""

        addedSeries.sortedBy { it.title }.forEach {
          if (it.title.first().toString() != currentLetter) {
            currentLetter = it.title.first().toString()
            Text1(currentLetter, style = MaterialTheme.typography.titleSmall)
          }

          Text1(it.title, style = MaterialTheme.typography.bodyMedium)
        }
      }
    }
  }

  RenderHeaderTitle("Recently Added Series", onTap = {
    showBottomSheet = true
  })

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Min),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .height(IntrinsicSize.Min),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      addedSeries.subList(0, 9).forEach { item ->
        Text(
          text = item.title,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
    Column(
      modifier = Modifier
        .weight(1f)
        .height(IntrinsicSize.Min),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      addedSeries.subList(10, 19).forEach { item ->
        Text(
          text = item.title,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderOngoingSeries(
  ongoingSeries: List<OngoingSeries>,
) {
  val sheetState = rememberModalBottomSheetState()
  var showBottomSheet by remember { mutableStateOf(false) }

  if (showBottomSheet) {
    ModalBottomSheet(
      sheetState = sheetState,
      onDismissRequest = {
        showBottomSheet = false
      },
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp, 16.dp, 0.dp, 16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text1("Recently Added Series", style = MaterialTheme.typography.titleMedium)
        var currentLetter = ""

        ongoingSeries.sortedBy { it.title }.forEach {
          if (it.title.first().toString() != currentLetter) {
            currentLetter = it.title.first().toString()
            Text1(currentLetter, style = MaterialTheme.typography.titleSmall)
          }

          Text1(it.title, style = MaterialTheme.typography.bodyMedium)
        }
      }
    }
  }

  RenderHeaderTitle("Ongoing Series", onTap = {
    showBottomSheet = true
  })
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Min),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .height(IntrinsicSize.Min),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      ongoingSeries.subList(0, 9).forEach { item ->
        Text(
          text = item.title,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
    Column(
      modifier = Modifier
        .weight(1f)
        .height(IntrinsicSize.Min),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      ongoingSeries.subList(10, 19).forEach { item ->
        Text(
          text = item.title,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
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
    Text1(
      text = title,
      style = MaterialTheme.typography.titleSmall,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
    )
    if (withViewAll) {
      Text1(
        text = "View All",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(
  viewModel: HomeViewModel = viewModel()
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        if (message.value != null) {
          Text1(message.value!!)
        }
        RenderSelectionPageCard()
        RenderOptionCard()
        homeResponse.value?.data?.let { RenderRecentRelease(it.recentRelease) }
        homeResponse.value?.data?.let { RenderPopularOngoingUpdate(it.popularOngoingUpdate) }
        homeResponse.value?.data?.let {
          RenderRecentlyAddedSeries(it.recentlyAddedSeries)
        }
        homeResponse.value?.data?.let { RenderOngoingSeries(it.ongoingSeries) }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  GogoanimeTheme {
    HomeActivity()
  }
}