package com.seiortech.gogoanime.features.popularongoing

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularOngoingResponse(
  val status: Int,
  val message: String,
  val data: List<AnimeItem>
) : Parcelable

@Parcelize
data class AnimeItem(
  @SerializedName("detail_slug") val detailSlug: String,
  val title: String,
  val img: String,
  val episode: String,
  val url: String,
  val genre: List<String>
) : Parcelable
