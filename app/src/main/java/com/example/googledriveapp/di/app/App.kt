package com.example.googledriveapp.di.app

import android.app.Application
import com.example.googledriveapp.di.AppComponent
import com.example.googledriveapp.di.AppModule
import com.example.googledriveapp.di.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}