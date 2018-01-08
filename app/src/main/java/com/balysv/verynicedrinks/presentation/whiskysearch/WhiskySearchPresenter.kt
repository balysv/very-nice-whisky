package com.balysv.verynicedrinks.presentation.whiskysearch

import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.bindAsyncSchedulers
import com.balysv.verynicedrinks.interactor.WhiskySearchInteractor
import com.balysv.verynicedrinks.interactor.WhiskySearchViewState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import javax.inject.Inject

@PerActivity
class WhiskySearchPresenter
@Inject constructor(private val whiskySearchInteractor: WhiskySearchInteractor)
  : MviBasePresenter<WhiskySearchView, WhiskySearchViewState>() {

  override fun bindIntents() {
    val searchViewStateObservable = intent(WhiskySearchView::searchIntent)
      .switchMap { whiskySearchInteractor.search(it.toString()) }
      .bindAsyncSchedulers()

    subscribeViewState(searchViewStateObservable, WhiskySearchView::render)
  }
}
