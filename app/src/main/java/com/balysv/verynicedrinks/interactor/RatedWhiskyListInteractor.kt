package com.balysv.verynicedrinks.interactor

import com.balysv.verynicedrinks.domain.randomfact.RandomFact
import com.balysv.verynicedrinks.domain.randomfact.RandomFactRepository
import com.balysv.verynicedrinks.domain.rating.Rating
import com.balysv.verynicedrinks.domain.rating.RatingRepository
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RatedWhiskyListInteractor(
  private val whiskyRepository: WhiskyRepository,
  private val ratingRepository: RatingRepository,
  private val randomFactRepository: RandomFactRepository
) {

  fun myRatings(): Observable<out MyRatingsViewState> {
    val randomFactObservable = randomFactRepository.findAny()
      .toObservable()
      .map { MyRatingsLoadingFact(it) as MyRatingsViewState }
      .doOnError({ Timber.e(it) })
      .onErrorResumeNext(Observable.just(MyRatingsLoading))
      .startWith(Observable.just(MyRatingsLoading))

    return ratingRepository.findAll()
      .filter({ !it.isEmpty() })
      .flatMap { ratings ->
        val whiskyIds = ratings.map { it.whiskyId }.distinct()
        val ratingMap = ratings.map { it.whiskyId to it }.toMap()
        whiskyRepository.findAll(whiskyIds)
          .map { it.map({ RatedWhisky(it, ratingMap[it.id]!!) }) }
      }
      .toObservable()
      .delay(3, TimeUnit.SECONDS)
      .map { MyRatingsLoaded(it) as MyRatingsViewState }
      .defaultIfEmpty(MyRatingsLoaded(emptyList()))
      .startWith(randomFactObservable)
      .onErrorReturn { MyRatingsError(it) }
  }
}

data class RatedWhisky(
  val whisky: Whisky,
  val rating: Rating
)

sealed class MyRatingsViewState
object MyRatingsLoading : MyRatingsViewState()
data class MyRatingsLoadingFact(val randomFact: RandomFact) : MyRatingsViewState()
data class MyRatingsLoaded(val ratedWhiskies: List<RatedWhisky>) : MyRatingsViewState()
data class MyRatingsError(val error: Throwable) : MyRatingsViewState()