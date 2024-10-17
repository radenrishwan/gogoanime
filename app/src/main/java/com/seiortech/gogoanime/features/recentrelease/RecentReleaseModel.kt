package com.seiortech.gogoanime.features.recentrelease

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecentReleaseResponse(
  val status: Int,
  val message: String,
  val data: List<AnimeItem>
) : Parcelable

@Parcelize
data class AnimeItem(
  @SerializedName("episode_slug") val episodeSlug: String,
  val title: String,
  val url: String,
  val img: String,
  val episode: Int,
) : Parcelable
