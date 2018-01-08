package com.balysv.verynicedrinks.interactor

import com.balysv.verynicedrinks.domain.rating.Niceness.VERY_NICE
import com.balysv.verynicedrinks.domain.rating.Niceness.VERY_VERY_NICE
import com.balysv.verynicedrinks.domain.rating.Rating
import com.balysv.verynicedrinks.domain.rating.RatingRepository
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier.FIFTH
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier.FOURTH
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import com.balysv.verynicedrinks.domain.whisky.WhiskyType.BLEND
import com.balysv.verynicedrinks.domain.whisky.WhiskyType.MALT
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RatedWhiskyListInteractorTest {

  @Mock
  lateinit var whiskyRepository: WhiskyRepository

  @Mock
  lateinit var ratingRepository: RatingRepository

  @InjectMocks
  lateinit var ratedWhiskyListInteractor: RatedWhiskyListInteractor

  @Test
  fun noRatings_OmitsViewStates_LoadingThenLoadedWithoutResults() {
    `when`(ratingRepository.findAll()).thenReturn(Flowable.just(emptyList()))

    val testSubscriber = TestObserver<MyRatingsViewState>()
    ratedWhiskyListInteractor.myRatings().subscribe(testSubscriber)

    testSubscriber.assertValues(MyRatingsLoading, MyRatingsLoaded(emptyList()))
  }

  @Test
  fun someRatings_OmitsViewStates_LoadingThenLoadedWithRatedWhiskies() {
    val hakushu = Whisky(1, "Hakushu 12YO", FIFTH, MALT, "Japan")
    val hakushuRating = Rating(1, 1, VERY_VERY_NICE)
    val hibiki = Whisky(2, "Hibiki Harmony", FOURTH, BLEND, "Japan")
    val hibikiRating = Rating(2, 2, VERY_NICE)

    `when`(ratingRepository.findAll()).thenReturn(Flowable.just(listOf(hakushuRating, hibikiRating)))
    `when`(whiskyRepository.findAll(arrayListOf(1, 2))).thenReturn(Flowable.just(listOf(hakushu, hibiki)))

    val testSubscriber = TestObserver<MyRatingsViewState>()
    ratedWhiskyListInteractor.myRatings().subscribe(testSubscriber)

    testSubscriber.assertValues(
      MyRatingsLoading,
      MyRatingsLoaded(listOf(
        RatedWhisky(hakushu, hakushuRating),
        RatedWhisky(hibiki, hibikiRating)))
    )
  }

  @Test
  fun ratedWhiskyDetailsMissing_OmitsViewState_LoadingThenLoadedWithRatedWhiskiesOmittingMissingOne() {
    val hakushu = Whisky(1, "Hakushu 12YO", FIFTH, MALT, "Japan")
    val hakushuRating = Rating(1, 1, VERY_VERY_NICE)
    val hibikiRating = Rating(2, 2, VERY_NICE)

    `when`(ratingRepository.findAll()).thenReturn(Flowable.just(listOf(hakushuRating, hibikiRating)))
    `when`(whiskyRepository.findAll(arrayListOf(1, 2))).thenReturn(Flowable.just(listOf(hakushu)))

    val testSubscriber = TestObserver<MyRatingsViewState>()
    ratedWhiskyListInteractor.myRatings().subscribe(testSubscriber)

    testSubscriber.assertValues(
      MyRatingsLoading,
      MyRatingsLoaded(listOf(RatedWhisky(hakushu, hakushuRating)))
    )
  }
}