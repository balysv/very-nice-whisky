package com.balysv.verynicedrinks

import com.balysv.verynicedrinks.presentation.launcher.LauncherActivity
import com.balysv.verynicedrinks.presentation.myratings.MyRatingsActivity
import com.balysv.verynicedrinks.presentation.ratewhisky.RateWhiskyActivity
import com.balysv.verynicedrinks.presentation.whiskysearch.WhiskySearchActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
  fun inject(application: VeryNiceApplication)

  fun launcherActivity(): LauncherActivity.Component.Builder
  fun whiskySearchActivity(): WhiskySearchActivity.Component.Builder
  fun rateWhiskyActivity(): RateWhiskyActivity.Component.Builder
  fun myRatingsActivity(): MyRatingsActivity.Component.Builder
}