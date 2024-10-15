package com.seiortech.gogoanime.features.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeResponse(
  val status: Int,
  val message: String,
  val data: Data
) : Parcelable

@Parcelize
data class Data(
  @SerializedName("recent_release") val recentRelease: List<RecentRelease>,
  @SerializedName("recently_added_series") val recentlyAddedSeries: List<RecentlyAddedSeries>,
  @SerializedName("ongoing_series") val ongoingSeries: List<OngoingSeries>,
  @SerializedName("popular_ongoing_update") val popularOngoingUpdate: List<PopularOngoingUpdate>
) : Parcelable

@Parcelize
data class RecentRelease(
  @SerializedName("episode_slug") val episodeSlug: String,
  val title: String,
  val img: String,
  val eps: String,
  val url: String
) : Parcelable

@Parcelize
data class RecentlyAddedSeries(
  @SerializedName("detail_slug") val detailSlug: String,
  val url: String,
  val title: String
) : Parcelable

@Parcelize
data class OngoingSeries(
  @SerializedName("detail_slug") val detailSlug: String,
  val url: String,
  val title: String
) : Parcelable

@Parcelize
data class PopularOngoingUpdate(
  @SerializedName("detail_slug") val detailSlug: String,
  val title: String,
  val img: String,
  val episode: String,
  val url: String,
  val genre: List<String>
) : Parcelable
