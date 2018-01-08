package com.balysv.verynicedrinks.interactor

import com.balysv.verynicedrinks.domain.rating.Niceness
import com.balysv.verynicedrinks.domain.rating.Rating
import com.balysv.verynicedrinks.domain.rating.RatingRepository
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import io.reactivex.Observable

class RateWhiskyInteractor(
  private val whiskyRepository: WhiskyRepository,
  private val ratingRepository: RatingRepository
) {

  fun whiskyDetails(whiskyId: Long): Observable<out RateWhiskyViewState> {
    return whiskyRepository.find(whiskyId).toSingle()
      .flatMap { whisky ->
        ratingRepository.findByWhiskyId(whiskyId).toSingle()
          .map { RateWhiskyOpenForm(whisky, it.niceness) }
          .onErrorReturn { RateWhiskyOpenForm(whisky, Niceness.NICE) }
      }
      .cast(RateWhiskyViewState::class.java)
      .onErrorReturn { RateWhiskyFormError(it) }
      .toObservable()
      .startWith(RateWhiskyFormLoading)
  }

  fun submit(whiskyId: Long, niceness: Niceness): Observable<out RateWhiskyViewState> {
    return ratingRepository.findByWhiskyId(whiskyId)
      .flatMapSingleElement {
        ratingRepository.modify(it.id, it.copy(whiskyId = whiskyId, niceness = niceness))
      }
      .switchIfEmpty(ratingRepository.add(Rating(0, whiskyId, niceness)))
      .toObservable()
      .map { RateWhiskyFormSubmitSuccessful as RateWhiskyViewState }
      .startWith(RateWhiskyFormSubmitting)
      .onErrorReturn { RateWhiskyFormSubmitError(it) }
  }
}

sealed class RateWhiskyViewState
object RateWhiskyFormLoading : RateWhiskyViewState()
data class RateWhiskyFormError(val error: Throwable) : RateWhiskyViewState()
data class RateWhiskyOpenForm(val whisky: Whisky, val niceness: Niceness?) : RateWhiskyViewState()
object RateWhiskyFormSubmitting : RateWhiskyViewState()
object RateWhiskyFormSubmitSuccessful : RateWhiskyViewState()
data class RateWhiskyFormSubmitError(val error: Throwable) : RateWhiskyViewState()
