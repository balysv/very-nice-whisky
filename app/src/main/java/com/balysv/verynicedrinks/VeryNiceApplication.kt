package com.balysv.verynicedrinks

import android.app.Application
import com.balysv.verynicedrinks.domain.RepositoryModule
import timber.log.Timber
import timber.log.Timber.DebugTree


class VeryNiceApplication : Application() {

  companion object {
    @JvmStatic lateinit var appGraph: AppComponent
  }

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }

    appGraph = DaggerAppComponent.builder()
        .appModule(AppModule())
        .androidModule(AndroidModule(this))
        .repositoryModule(RepositoryModule())
        .build()
    appGraph.inject(this)
  }
}