package com.balysv.verynicedrinks.interactor

import com.balysv.verynicedrinks.domain.rating.Niceness.*
import com.balysv.verynicedrinks.domain.rating.Rating
import com.balysv.verynicedrinks.domain.rating.RatingRepository
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier.*
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import com.balysv.verynicedrinks.domain.whisky.WhiskyType.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RateWhiskyInteractorTest {

  @Mock
  private lateinit var ratingRepository: RatingRepository

  @Mock
  private lateinit var whiskyRepository: WhiskyRepository

  @InjectMocks
  private lateinit var rateWhiskyInteractor: RateWhiskyInteractor

  @Test
  fun whiskyDetailsExistingWithRating_OmitsViewStates_FormLoadingThenOpenForm() {
    val whiskyId: Long = 1
    val hakushu = Whisky(whiskyId, "Hakushu 12yo", FOURTH, MALT, "Japan")
    val rating = Rating(0, whiskyId, VERY_VERY_NICE)

    `when`(whiskyRepository.find(whiskyId)).thenReturn(Maybe.just(hakushu))
    `when`(ratingRepository.findByWhiskyId(whiskyId)).thenReturn(Maybe.just(rating))

    val testObserver = TestObserver<RateWhiskyViewState>()
    rateWhiskyInteractor.whiskyDetails(whiskyId).subscribe(testObserver)

    testObserver.assertValues(
      RateWhiskyFormLoading,
      RateWhiskyOpenForm(hakushu, rating.niceness)
    )
  }

  @Test
  fun whiskyDetailsExistingWithoutRating_OmitsViewStates_FormLoadingThenOpenForm() {
    val whiskyId: Long = 1
    val hakushu = Whisky(whiskyId, "Hakushu 12yo", FOURTH, MALT, "Japan")
    `when`(whiskyRepository.find(whiskyId)).thenReturn(Maybe.just(hakushu))
    `when`(ratingRepository.findByWhiskyId(whiskyId)).thenReturn(Maybe.empty())

    val testObserver = TestObserver<RateWhiskyViewState>()
    rateWhiskyInteractor.whiskyDetails(whiskyId).subscribe(testObserver)

    testObserver.assertValues(
      RateWhiskyFormLoading,
      RateWhiskyOpenForm(hakushu, NICE) // default niceness
    )
  }

  @Test
  fun whiskyDetailsMissing_OmitsViewStates_FormLoadingThenFormError() {
    val whiskyId: Long = 1
    `when`(whiskyRepository.find(whiskyId)).thenReturn(Maybe.empty())

    val testObserver = TestObserver<RateWhiskyViewState>()
    rateWhiskyInteractor.whiskyDetails(whiskyId).subscribe(testObserver)

    testObserver.assertValues(
      RateWhiskyFormLoading,
      RateWhiskyFormError(NoSuchElementException()) // todo how to assert correct exceptions
    )
  }

  @Test
  fun addNewRating_OmitsViewStates_SubmittingThenSubmitSuccessfull() {
    val whiskyId: Long = 1
    val niceness = VERY_VERY_NICE

    val expected = Rating(0, whiskyId, niceness)
    `when`(ratingRepository.findByWhiskyId(whiskyId)).thenReturn(Maybe.empty())
    `when`(ratingRepository.add(expected)).thenReturn(Single.just(expected))

    val testObserver = TestObserver<RateWhiskyViewState>()
    rateWhiskyInteractor.submit(whiskyId, niceness).subscribe(testObserver)

    verify(ratingRepository, times(0)).modify(whiskyId, expected)
    testObserver.assertValues(
      RateWhiskyFormSubmitting,
      RateWhiskyFormSubmitSuccessful
    )
  }

  @Test
  fun updateExistingRating_OmitsViewStates_SubmittingThenSubmitSuccessfull() {
    val whiskyId: Long = 1
    val niceness = VERY_VERY_NICE

    val current = Rating(1, whiskyId, NOT_NICE)
    val expected = current.copy(niceness = niceness)

    `when`(ratingRepository.add(expected.copy(id = 0))).thenReturn(Single.error(IllegalStateException("")))
    `when`(ratingRepository.findByWhiskyId(whiskyId)).thenReturn(Maybe.just(current))
    `when`(ratingRepository.modify(current.id, expected)).thenReturn(Single.just(expected))

    val testObserver = TestObserver<RateWhiskyViewState>()
    rateWhiskyInteractor.submit(whiskyId, niceness).subscribe(testObserver)

    verify(ratingRepository).modify(whiskyId, expected)
    testObserver.assertValues(
      RateWhiskyFormSubmitting,
      RateWhiskyFormSubmitSuccessful
    )
  }
}