package com.seiortech.gogoanime.features.detail

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

interface DetailApiService {
  @GET("details/{slug}")
  suspend fun getDetail(@Path("slug") slug: String): Response<DetailResponse>
}


fun getRetrofitClient(): DetailApiService {
  return Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(DetailApiService::class.java)
}

class DetailRepository(
  private val apiService: DetailApiService = getRetrofitClient()
) {
  suspend fun getDetail(slug: String): Response<DetailResponse> {
    return apiService.getDetail(slug)
  }
}

class DetailViewModel(
  private val repository: DetailRepository = DetailRepository()
) : ViewModel() {
  private var _detailResponse = MutableStateFlow<DetailResponse?>(null)
  private var _message = MutableStateFlow<String?>(null)
  private var _isLoading = MutableStateFlow(false)

  val detailResponse get() = _detailResponse.asStateFlow()
  val message get() = _message.asStateFlow()
  val isLoading get() = _isLoading.asStateFlow()

  suspend fun getDetail(slug: String) {
    _isLoading.value = true
    repository.getDetail(slug).let { response ->
      if (response.isSuccessful) {
        _detailResponse.value = response.body()
      } else {
        _message.value = response.errorBody()?.string()
      }
    }
    _isLoading.value = false
  }
}
