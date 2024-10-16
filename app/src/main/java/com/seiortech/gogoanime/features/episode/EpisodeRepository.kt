package com.seiortech.gogoanime.features.episode

import androidx.lifecycle.ViewModel
import com.seiortech.gogoanime.BASE_URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApiService {
  @GET("episode/{episode_slug}")
  suspend fun getEpisodeDetails(@Path("episode_slug") episodeSlug: String): Response<EpisodeResponse>
}

fun getEpisodeRetrofitClient(): EpisodeApiService {
  return Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(EpisodeApiService::class.java)
}

class EpisodeRepository(
  private val apiService: EpisodeApiService = getEpisodeRetrofitClient()
) {
  suspend fun getEpisodeDetails(episodeSlug: String): Response<EpisodeResponse> {
    return apiService.getEpisodeDetails(episodeSlug)
  }
}

class EpisodeViewModel(
  private val repository: EpisodeRepository = EpisodeRepository()
) : ViewModel() {
  private var _detailResponse = MutableStateFlow<EpisodeResponse?>(null)
  private var _message = MutableStateFlow<String?>(null)
  private var _isLoading = MutableStateFlow(false)

  val detailResponse get() = _detailResponse.asStateFlow()
  val message get() = _message.asStateFlow()
  val isLoading get() = _isLoading.asStateFlow()

  suspend fun getEpisode(slug: String) {
    _isLoading.value = true
    repository.getEpisodeDetails(slug).let { response ->
      if (response.isSuccessful) {
        _detailResponse.value = response.body()
      } else {
        _message.value = response.errorBody()?.string()
      }
    }
    _isLoading.value = false
  }
}
