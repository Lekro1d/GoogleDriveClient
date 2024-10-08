package com.example.googledriveapp.di

import com.example.googledriveapp.presentation.view.activity.LoginActivity
import com.example.googledriveapp.presentation.view.fragments.AccountFragment
import com.example.googledriveapp.presentation.view.fragments.MenuFragment
import com.example.googledriveapp.presentation.view.fragments.UploadFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [GoogleDriveModule::class, AppModule::class, GoogleAccountModule::class])
@Singleton
interface AppComponent {
    fun inject(loginActivity: LoginActivity)

    fun inject(menuFragment: MenuFragment)
    fun inject(uploadFragment: UploadFragment)
    fun inject(accountFragment: AccountFragment)
}