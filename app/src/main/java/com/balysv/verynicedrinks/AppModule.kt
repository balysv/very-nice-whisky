package com.balysv.verynicedrinks

import com.balysv.verynicedrinks.infrastructure.ApiModule
import com.balysv.verynicedrinks.infrastructure.PersistenceModule
import com.balysv.verynicedrinks.domain.RepositoryModule
import com.balysv.verynicedrinks.presentation.launcher.LauncherActivity
import com.balysv.verynicedrinks.presentation.whiskysearch.WhiskySearchActivity
import dagger.Module

@Module(
  includes = arrayOf(
    AndroidModule::class,
    PersistenceModule::class,
    ApiModule::class,
    RepositoryModule::class
  ),
  subcomponents = arrayOf(
    LauncherActivity.Component::class,
    WhiskySearchActivity.Component::class
  ))
class AppModule