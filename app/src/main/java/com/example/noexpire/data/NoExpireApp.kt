package com.example.noexpire.data

import android.app.Application

class NoExpireApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.appContext = this // Initialize appContext here
        Graph.provide(this)
    }
}
