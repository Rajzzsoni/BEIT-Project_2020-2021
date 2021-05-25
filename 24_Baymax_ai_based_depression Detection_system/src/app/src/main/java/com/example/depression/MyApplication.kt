package com.example.depression

import android.app.Application
import com.example.depression.module.firebaseViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate()
    {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(firebaseViewModelModule))
        }
    }
}