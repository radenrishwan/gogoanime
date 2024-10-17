package com.seiortech.gogoanime.features.recentrelease

import android.util.Log
import androidx.lifecycle.ViewModel
import com.seiortech.gogoanime.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RecentReleaseApiService {
  @GET("recent-release")
  suspend fun getRecentReleases(@Query("page") page: Int): Response<RecentReleaseResponse>
}

fun getRetrofitClient(): RecentReleaseApiService {
  return Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(RecentReleaseApiService::class.java)
}

class RecentReleaseRepository(
  private val apiService: RecentReleaseApiService = getRetrofitClient()
) {
  suspend fun getRecentReleases(page: Int): Response<RecentReleaseResponse> {
    return apiService.getRecentReleases(page)
  }

  suspend fun loadMoreRecentReleases(page: Int): Response<RecentReleaseResponse> {
    return apiService.getRecentReleases(page)
  }
}

class RecentReleaseViewModel(
  private val repository: RecentReleaseRepository = RecentReleaseRepository()
) : ViewModel() {
  private var _recentReleaseResponse = MutableStateFlow<RecentReleaseResponse?>(null)
  private var _message = MutableStateFlow<String?>(null)
  private var _isLoading = MutableStateFlow(false)

  val recentReleaseResponse get() = _recentReleaseResponse.asStateFlow()
  val message get() = _message.asStateFlow()
  val isLoading get() = _isLoading.asStateFlow()

  suspend fun getRecentReleases(page: Int) {
    _isLoading.value = true
    try {
      val response = withContext(Dispatchers.IO) {
        repository.getRecentReleases(page)
      }
      if (response.isSuccessful) {
        _recentReleaseResponse.value = response.body()
        Log.d("RecentReleaseViewModel", "Response: ${response.body()}")
      } else {
        _message.value = response.errorBody()?.string()
        Log.e("RecentReleaseViewModel", "Error: ${response.errorBody()?.string()}")
      }
    } catch (e: Exception) {
      _message.value = e.message
      Log.e("RecentReleaseViewModel", "Exception: ${e.message}")
    } finally {
      _isLoading.value = false
    }
  }

  suspend fun loadMoreRecentReleases(page: Int) {
    _isLoading.value = true
    try {
      val response = withContext(Dispatchers.IO) {
        repository.loadMoreRecentReleases(page)
      }
      if (response.isSuccessful) {
        val res = response.body()

        // add the new data to the existing data
        val currentData = _recentReleaseResponse.value?.data ?: emptyList()

        _recentReleaseResponse.value = res?.copy(data = currentData + res.data)

        Log.d("RecentReleaseViewModel", "Length: ${res?.data?.size}, Response: ${response.body()}")
      } else {
        _message.value = response.errorBody()?.string()
        Log.e("RecentReleaseViewModel", "Error: ${response.errorBody()?.string()}")
      }
    } catch (e: Exception) {
      _message.value = e.message
      Log.e("RecentReleaseViewModel", "Exception: ${e.message}")
    } finally {
      _isLoading.value = false
    }
  }

}
