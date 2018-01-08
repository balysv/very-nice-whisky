package com.balysv.verynicedrinks.interactor

import android.content.Context
import com.balysv.verynicedrinks.R
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import com.balysv.verynicedrinks.domain.whisky.WhiskyType
import com.opencsv.CSVReader
import io.reactivex.Maybe
import io.reactivex.Observable
import java.io.InputStreamReader

class PreloadWhiskyInteractor(
  private val app: Context,
  private val whiskyRepository: WhiskyRepository
) {

  fun preload(): Observable<out PreloadWhiskyViewState> {
    return whiskyRepository.isEmpty()
      .filter { it }
      .flatMap {
        val reader = CSVReader(InputStreamReader(app.resources.openRawResource(R.raw.whisky_db)))
        Maybe.just(reader.map {
          Whisky(
            -1L,
            it[0],
            mapCostTier(it[4]),
            mapType(it[9]),
            it[8]
          )
        })
      }
      .flatMapCompletable { whiskyRepository.addAll(it) }
      .andThen(Observable.just<PreloadWhiskyViewState>(PreloadWhiskyComplete))
      .startWith(Observable.just(PreloadWhiskyLoading))
      .onErrorReturn { PreloadWhiskyError(it) }
  }

  private fun mapCostTier(input: String): WhiskyCostTier {
    return when (input) {
      "$" -> WhiskyCostTier.FIRST
      "$$" -> WhiskyCostTier.SECOND
      "$$$" -> WhiskyCostTier.THIRD
      "$$$$" -> WhiskyCostTier.FOURTH
      "$$$$$" -> WhiskyCostTier.FIFTH
      "$$$$$+" -> WhiskyCostTier.SIXTH
      "" -> WhiskyCostTier.UNKNOWN
      else -> throw IllegalStateException("Invalid CostTier $input")
    }
  }

  private fun mapType(input: String): WhiskyType {
    return if (input.isBlank())
      WhiskyType.UNKNOWN
    else
      WhiskyType.valueOf(input.toUpperCase())
  }
}

sealed class PreloadWhiskyViewState
object PreloadWhiskyLoading : PreloadWhiskyViewState()
object PreloadWhiskyComplete : PreloadWhiskyViewState()
data class PreloadWhiskyError(val error: Throwable) : PreloadWhiskyViewState()