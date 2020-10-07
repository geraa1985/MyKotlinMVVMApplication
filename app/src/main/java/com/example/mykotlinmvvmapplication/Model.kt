package com.example.mykotlinmvvmapplication

class Model constructor(): IModel {

    override fun getdata(): String {
        return "Hello!"
    }

}