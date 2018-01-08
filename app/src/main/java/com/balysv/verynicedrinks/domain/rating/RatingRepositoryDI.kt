package com.balysv.verynicedrinks.domain.rating

import com.balysv.verynicedrinks.infrastructure.db.dao.RatingDao
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class RatingRepositoryScope

@RatingRepositoryScope
@Subcomponent(modules = arrayOf(RatingRepositoryModule::class))
interface RatingRepositoryComponent {
  @RatingRepositoryScope
  fun ratingRepository(): RatingRepository

  @Subcomponent.Builder
  interface Builder {
    fun module(module: RatingRepositoryModule): Builder
    fun build(): RatingRepositoryComponent
  }
}

@Module
class RatingRepositoryModule {
  @Provides
  @RatingRepositoryScope
  fun ratingRepositoryImpl(dao: RatingDao): RatingRepository = RatingRepositoryImpl(dao)
}