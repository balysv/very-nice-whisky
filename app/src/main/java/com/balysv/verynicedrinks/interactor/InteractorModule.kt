package com.balysv.verynicedrinks.interactor

import android.content.Context
import com.balysv.verynicedrinks.ForApplication
import com.balysv.verynicedrinks.domain.randomfact.RandomFactRepository
import com.balysv.verynicedrinks.domain.rating.RatingRepository
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

  @Provides
  fun preloadWhiskyInteractor(
    @ForApplication app: Context,
    whiskyRepository: WhiskyRepository
  ) = PreloadWhiskyInteractor(app, whiskyRepository)

  @Provides
  fun ratedWhiskyListInteracvtor(
    whiskyRepository: WhiskyRepository,
    ratingRepository: RatingRepository,
    randomFactRepository: RandomFactRepository
  ) = RatedWhiskyListInteractor(whiskyRepository, ratingRepository, randomFactRepository)

  @Provides
  fun rateWhiskyInteractor(
    whiskyRepository: WhiskyRepository,
    ratingRepository: RatingRepository
  ) = RateWhiskyInteractor(whiskyRepository, ratingRepository)

  @Provides
  fun searchWhiskyInteractor(
    whiskyRepository: WhiskyRepository
  ) = WhiskySearchInteractor(whiskyRepository)
}