package com.example.mykotlinmvvmapplication

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Model @Inject constructor(): IModel {

    override fun getdata(): String {
        return "Hello!"
    }

}