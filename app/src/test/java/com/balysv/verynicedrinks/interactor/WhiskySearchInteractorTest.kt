package com.balysv.verynicedrinks.interactor

import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import com.balysv.verynicedrinks.domain.whisky.WhiskyType
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WhiskySearchInteractorTest {

  @Mock
  lateinit var whiskyRepository: WhiskyRepository

  @InjectMocks
  lateinit var whiskySearchInteractor: WhiskySearchInteractor

  @Before
  fun before() {
    `when`(whiskyRepository.search(Matchers.anyString())).thenReturn(Flowable.just(emptyList()))
  }

  @Test
  fun queryTrimmed() {
    val query = "   abd   "
    val trimmedQuery = "abd"

    val testSubscriber = TestObserver<WhiskySearchViewState>()
    whiskySearchInteractor.search(query).subscribe(testSubscriber)

    verify(whiskyRepository).search(trimmedQuery)
  }

  @Test
  fun queryShorterThanThreeCharacters_OmitsViewState_SearchNotStarted() {
    with(TestObserver<WhiskySearchViewState>(), {
      whiskySearchInteractor.search("").subscribe(this)
      assertValue(WhiskySearchNotStarted)
    })

    with(TestObserver<WhiskySearchViewState>(), {
      whiskySearchInteractor.search("a").subscribe(this)
      assertValue(WhiskySearchNotStarted)
    })

    with(TestObserver<WhiskySearchViewState>(), {
      whiskySearchInteractor.search("ab").subscribe(this)
      assertValue(WhiskySearchNotStarted)
    })
  }


  @Test
  fun queryThreeOrMoreCharactersWithResults_OmitsViewStates_LoadingThenResults() {
    val query = "abcd"

    val result = Whisky(1, "", WhiskyCostTier.FIFTH, WhiskyType.BARLEY, "")
    `when`(whiskyRepository.search(query)).thenReturn(Flowable.just(listOf(result)))

    val testSubscriber = TestObserver<WhiskySearchViewState>()
    whiskySearchInteractor.search(query).subscribe(testSubscriber)

    verify(whiskyRepository).search(query)
    testSubscriber.assertValues(
      WhiskySearchLoading,
      WhiskySearchResults(query, listOf(result))
    )
  }

  @Test
  fun queryThreeOrMoreCharactersNoResults_OmitsViewStates_LoadingThenNoResults() {
    val query = "abcd"

    val testSubscriber = TestObserver<WhiskySearchViewState>()
    whiskySearchInteractor.search(query).subscribe(testSubscriber)

    verify(whiskyRepository).search(query)
    testSubscriber.assertValues(
      WhiskySearchLoading,
      WhiskySearchEmpty(query)
    )
  }
}