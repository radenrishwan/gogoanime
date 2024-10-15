package com.seiortech.gogoanime.features.home

import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import retrofit2.Response

private const val BASE_URL = "https://gogoanime-api-500987716325.asia-southeast2.run.app/api/"

interface HomeApiService {
  @GET("home")
  suspend fun getHome(): Response<HomeResponse>
}

fun getRetrofitClient(): HomeApiService {
  return Retrofit.Builder()
    .baseUrl(BASE_URL) // Specify your base URL
    .addConverterFactory(GsonConverterFactory.create()) // Specify JSon convertion method
    .client(OkHttpClient())// Add converter factory for Gson
    .build()
    .create(HomeApiService::class.java)
}

class HomeRepository(
  private val apiService: HomeApiService = getRetrofitClient()
) {
  suspend fun getHome(): Response<HomeResponse> {
    return apiService.getHome()
  }
}

class HomeViewModel(
  private val repository: HomeRepository = HomeRepository()
) : ViewModel() {
  private var _homeResponse = MutableStateFlow<HomeResponse?>(null)
  private var _message = MutableStateFlow<String?>(null)
  private var _isLoading = MutableStateFlow(false)

  val homeResponse get() = _homeResponse.asStateFlow()
  val message get() = _message.asStateFlow()
  val isLoading get() = _isLoading.asStateFlow()

  suspend fun getHome() {
    _isLoading.value = true
    repository.getHome().let { response ->
      if (response.isSuccessful) {
        _homeResponse.value = response.body()
      } else {
        _message.value = response.errorBody()?.string()
      }
    }
    _isLoading.value = false
  }
}