package project.nasapictureoftheday

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NasaPictureOfTheDayModule {
    @Provides
    @ViewModelScoped
    fun providesApiService(): ApiService {
        return ApiServiceImpl()
    }
}