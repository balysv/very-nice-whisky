package com.balysv.verynicedrinks.presentation.myratings

import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.bindAsyncSchedulers
import com.balysv.verynicedrinks.interactor.*
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import javax.inject.Inject

@PerActivity
class MyRatingsPresenter
@Inject constructor(private val interactor: RatedWhiskyListInteractor)
  : MviBasePresenter<MyRatingsView, MyRatingsViewState>() {

  override fun bindIntents() {
    val searchViewStateObservable = intent(MyRatingsView::loadRatedWhiskiesIntent)
      .switchMap { interactor.myRatings().bindAsyncSchedulers() }
      .distinctUntilChanged()

    subscribeViewState(searchViewStateObservable, MyRatingsView::render)
  }
}
