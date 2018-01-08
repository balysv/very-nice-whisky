package com.balysv.verynicedrinks.presentation.ratewhisky

import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.interactor.RateWhiskyInteractor
import com.balysv.verynicedrinks.interactor.RateWhiskyViewState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

@PerActivity
class RateWhiskyPresenter
@Inject constructor(
  private val interactor: RateWhiskyInteractor,
  private val whiskyId: Long
) : MviBasePresenter<RateWhiskyView, RateWhiskyViewState>() {

  override fun bindIntents() {
    val detailsObservable = intent(RateWhiskyView::loadFormIntent)
      .flatMap { interactor.whiskyDetails(whiskyId).subscribeOn(io()) }
      .observeOn(mainThread())

    val submitObservable = intent(RateWhiskyView::submitFormIntent)
      .flatMap { interactor.submit(whiskyId, it).subscribeOn(io()) }
      .observeOn(mainThread())

    val combinedObservable = Observable.merge(detailsObservable, submitObservable)

    subscribeViewState(combinedObservable, RateWhiskyView::render)
  }
}
