package com.seiortech.gogoanime.features.home

import android.media.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun AnimeCard(modifier: Modifier = Modifier, imgUrl: String, title: String, episode: String) {
  Column(
    modifier = modifier
      .width(120.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    AsyncImage(
      model = imgUrl,
      contentDescription = title,
      modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .clip(MaterialTheme.shapes.medium),
      contentScale = ContentScale.Crop,
    )
    Text(
      text = title,
      textAlign = TextAlign.Start,
      modifier = Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.titleSmall,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
    Text(
      text = episode,
      textAlign = TextAlign.Start,
      modifier = Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.bodySmall.copy(
        color = Color.Gray,
      ),
    )
  }
}

@Composable
fun OptionCard(title: String, icon: ImageVector, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    ElevatedCard(
      elevation = CardDefaults.cardElevation(
        defaultElevation = 1.dp,
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(),
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          modifier = Modifier.size(24.dp),
        )
      }
    }
    Text(
      title,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodySmall,
    )
  }
}