package com.balysv.verynicedrinks.interactor

import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import io.reactivex.Flowable
import io.reactivex.Observable

class WhiskySearchInteractor(private val whiskyRepository: WhiskyRepository) {

  fun search(query: String): Observable<out WhiskySearchViewState> {
    val trimmedQuery = query.trim()
    if (trimmedQuery.length < 3) {
      return Observable.just(WhiskySearchNotStarted)
    }

    return Flowable.just(trimmedQuery)
      .flatMap { whiskyRepository.search(it) }
      .map {
        if (it.isEmpty()) {
          WhiskySearchEmpty(trimmedQuery)
        } else {
          WhiskySearchResults(trimmedQuery, it)
        }
      }
      .toObservable()
      .startWith(Observable.just(WhiskySearchLoading))
      .onErrorReturn { WhiskySearchError(trimmedQuery, it) }
  }
}

sealed class WhiskySearchViewState
object WhiskySearchNotStarted : WhiskySearchViewState()
object WhiskySearchLoading : WhiskySearchViewState()
data class WhiskySearchEmpty(val query: String) : WhiskySearchViewState()
data class WhiskySearchResults(val query: String, val results: List<Whisky>) : WhiskySearchViewState()
data class WhiskySearchError(val query: String, val error: Throwable) : WhiskySearchViewState()

