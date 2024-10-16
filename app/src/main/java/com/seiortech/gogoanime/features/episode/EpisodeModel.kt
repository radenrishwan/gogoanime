package com.seiortech.gogoanime.features.episode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeResponse(
  val status: Int,
  val message: String,
  val data: EpisodeData
) : Parcelable

@Parcelize
data class EpisodeData(
  val urls: List<String>
) : Parcelable