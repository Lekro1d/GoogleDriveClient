package com.example.googledriveapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = appContext
}