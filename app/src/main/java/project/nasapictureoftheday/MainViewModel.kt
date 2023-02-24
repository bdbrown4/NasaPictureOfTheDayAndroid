package project.nasapictureoftheday

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel() {
    suspend fun processRequest(apiKey: String, date: String): PictureOfTheDay {
        return apiService.getPictureOfTheDay(apiKey, date)
    }
}