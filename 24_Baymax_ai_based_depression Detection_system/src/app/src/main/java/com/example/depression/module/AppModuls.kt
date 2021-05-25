package com.example.depression.module

import com.example.depression.repository.FirebaseViewModel
import com.example.depression.repository.MainViewModel
import com.example.depression.repository.implementation.HomeRepositoryImpl
import com.example.depression.repository.implementation.UserRepositoryImpl
import com.example.depression.ui.home.HomeViewModel
import org.koin.dsl.module

val firebaseViewModelModule = module {
    single { FirebaseViewModel(UserRepositoryImpl()) }
    single { MainViewModel(UserRepositoryImpl()) }
    single { HomeViewModel(HomeRepositoryImpl()) }
}