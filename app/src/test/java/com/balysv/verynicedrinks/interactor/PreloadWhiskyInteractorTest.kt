package com.balysv.verynicedrinks.interactor

import android.content.res.Resources
import android.test.mock.MockContext
import com.balysv.verynicedrinks.R
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.domain.whisky.WhiskyRepository
import io.reactivex.Completable.*
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PreloadWhiskyInteractorTest {

  private val inputCsv = "\"Ledaig 42yo Old Dusgadh\",\"9.46\",\"0.24\",\"3\",\"\$\$\$\$\$+\",\"SingleMalt-like\",\"ABC\",\"C\",\"Scotland\",\"Malt\""

  @Mock
  lateinit var whiskyRepository: WhiskyRepository

  @Mock
  lateinit var app: MockContext

  @Mock
  lateinit var resources: Resources

  @InjectMocks
  lateinit var preloadWhiskyInteractor: PreloadWhiskyInteractor

  @Test
  fun preloadNotLoaded_OmitsViewStates_LoadingThenComplete() {
    `when`(whiskyRepository.isEmpty()).thenReturn(Single.just(true))
    `when`(whiskyRepository.addAll(anyCollectionOf(Whisky::class.java))).thenReturn(complete())
    `when`(app.resources).thenReturn(resources)
    `when`(resources.openRawResource(R.raw.whisky_db)).thenReturn(inputCsv.byteInputStream())

    val testSubscriber = TestObserver<PreloadWhiskyViewState>()
    preloadWhiskyInteractor.preload().subscribe(testSubscriber)

    testSubscriber.assertValues(
      PreloadWhiskyLoading,
      PreloadWhiskyComplete
    )
    verify(whiskyRepository).addAll(anyCollectionOf(Whisky::class.java))
  }

  @Test
  fun preloadAlreadyLoaded_OmitsViewStates_LoadingThenComplete() {
    `when`(whiskyRepository.isEmpty()).thenReturn(Single.just(false))

    val testSubscriber = TestObserver<PreloadWhiskyViewState>()
    preloadWhiskyInteractor.preload().subscribe(testSubscriber)

    testSubscriber.assertValues(
      PreloadWhiskyLoading,
      PreloadWhiskyComplete
    )
    verify(whiskyRepository, times(0)).findAll(anyCollectionOf(Long::class.java))
    verifyZeroInteractions(app)
  }
}