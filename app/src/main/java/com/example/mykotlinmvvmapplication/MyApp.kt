package com.example.mykotlinmvvmapplication

import android.app.Application
import com.example.mykotlinmvvmapplication.di.koin.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.startKoin

class MyApp : Application() {

    companion object {
        private var instance: MyApp? = null
    }

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(
                appModule,
                mainModule,
                noteModule,
                splashModule,
                logoutModule,
                deleteModule
        ))
    }

}