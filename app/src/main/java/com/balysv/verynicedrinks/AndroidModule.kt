package com.balysv.verynicedrinks

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: VeryNiceApplication) {

    @Provides
    @Singleton
    @ForApplication
    fun applicationContext(): Context = application
}