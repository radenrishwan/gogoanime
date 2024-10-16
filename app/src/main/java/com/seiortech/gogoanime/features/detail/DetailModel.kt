package com.seiortech.gogoanime.features.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailResponse(
  val status: Int,
  val message: String,
  val data: Data
) : Parcelable

@Parcelize
data class Data(
  val title: String,
  val img: String,
  val details: Details,
  val genres: List<String>,
  @SerializedName("other_titles") val otherTitles: List<String>,
  val episodes: List<Episode>
) : Parcelable

@Parcelize
data class Details(
  val type: String,
  val released: String,
  @SerializedName("plot_summary") val plotSummary: String,
  val status: String
) : Parcelable

@Parcelize
data class Episode(
  val title: Int,
  @SerializedName("episode_slug") val episodeSlug: String,
  val url: String
) : Parcelable
