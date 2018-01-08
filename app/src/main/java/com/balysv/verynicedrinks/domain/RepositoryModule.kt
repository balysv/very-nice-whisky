package com.balysv.verynicedrinks.domain

import com.balysv.verynicedrinks.domain.randomfact.RandomFactRepositoryComponent
import com.balysv.verynicedrinks.domain.randomfact.RandomFactRepositoryModule
import com.balysv.verynicedrinks.domain.rating.RatingRepositoryComponent
import com.balysv.verynicedrinks.domain.rating.RatingRepositoryModule
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepositoryComponent
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepositoryModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = arrayOf(
  WhiskyRepositoryComponent::class,
  RatingRepositoryComponent::class,
  RandomFactRepositoryComponent::class
))
class RepositoryModule {

  @Provides
  @Singleton
  fun whiskyRepository(componentBuilder: WhiskyRepositoryComponent.Builder) =
    componentBuilder
      .module(WhiskyRepositoryModule())
      .build()
      .whiskyRepository()

  @Provides
  @Singleton
  fun ratingRepository(componentBuilder: RatingRepositoryComponent.Builder) =
    componentBuilder
      .module(RatingRepositoryModule())
      .build()
      .ratingRepository()

  @Provides
  @Singleton
  fun randomFactRepository(componentBuilder: RandomFactRepositoryComponent.Builder) =
    componentBuilder
      .module(RandomFactRepositoryModule())
      .build()
      .randomFactRepository()
}