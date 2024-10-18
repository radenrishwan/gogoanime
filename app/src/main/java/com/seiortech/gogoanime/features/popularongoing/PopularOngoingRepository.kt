package com.seiortech.gogoanime.features.popularongoing

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

interface PopularOngoingApiService {
  @GET("popular-ongoing")
  suspend fun getPopularOngoings(@Query("page") page: Int): Response<PopularOngoingResponse>
}

fun getRetrofitClient(): PopularOngoingApiService {
  return Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(PopularOngoingApiService::class.java)
}

class PopularOngoingRepository(
  private val apiService: PopularOngoingApiService = getRetrofitClient()
) {
  suspend fun getPopularOngoings(page: Int): Response<PopularOngoingResponse> {
    return apiService.getPopularOngoings(page)
  }

  suspend fun loadMorePopularOngoings(page: Int): Response<PopularOngoingResponse> {
    return apiService.getPopularOngoings(page)
  }
}

class PopularOngoingViewModel(
  private val repository: PopularOngoingRepository = PopularOngoingRepository()
) : ViewModel() {
  private var _popularOngoingResponse = MutableStateFlow<PopularOngoingResponse?>(null)
  private var _message = MutableStateFlow<String?>(null)
  private var _isLoading = MutableStateFlow(false)

  val popularOngoingResponse get() = _popularOngoingResponse.asStateFlow()
  val message get() = _message.asStateFlow()
  val isLoading get() = _isLoading.asStateFlow()

  suspend fun getPopularOngoings(page: Int) {
    _isLoading.value = true
    try {
      val response = withContext(Dispatchers.IO) {
        repository.getPopularOngoings(page)
      }
      if (response.isSuccessful) {
        _popularOngoingResponse.value = response.body()
        Log.d("PopularOngoingViewModel", "Response: ${response.body()}")
      } else {
        _message.value = response.errorBody()?.string()
        Log.e("PopularOngoingViewModel", "Error: ${response.errorBody()?.string()}")
      }
    } catch (e: Exception) {
      _message.value = e.message
      Log.e("PopularOngoingViewModel", "Exception: ${e.message}")
    } finally {
      _isLoading.value = false
    }
  }

  suspend fun loadMorePopularOngoings(page: Int) {
    _isLoading.value = true
    try {
      val response = withContext(Dispatchers.IO) {
        repository.loadMorePopularOngoings(page)
      }
      if (response.isSuccessful) {
        val res = response.body()

        // add the new data to the existing data
        val currentData = _popularOngoingResponse.value?.data ?: emptyList()

        _popularOngoingResponse.value = res?.copy(data = currentData + res.data)

        Log.d("PopularOngoingViewModel", "Length: ${res?.data?.size}, Response: ${response.body()}")
      } else {
        _message.value = response.errorBody()?.string()
        Log.e("PopularOngoingViewModel", "Error: ${response.errorBody()?.string()}")
      }
    } catch (e: Exception) {
      _message.value = e.message
      Log.e("PopularOngoingViewModel", "Exception: ${e.message}")
    } finally {
      _isLoading.value = false
    }
  }

}
