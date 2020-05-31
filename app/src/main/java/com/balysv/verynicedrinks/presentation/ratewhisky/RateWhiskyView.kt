package com.balysv.verynicedrinks.presentation.ratewhisky

import android.content.Context
import androidx.transition.TransitionManager
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import com.balysv.verynicedrinks.interactor.*
import com.balysv.verynicedrinks.getDaggerComponent
import com.balysv.verynicedrinks.domain.rating.Niceness
import com.balysv.verynicedrinks.asActivity
import com.hannesdorfmann.mosby3.mvi.layout.MviFrameLayout
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.rate_whisky_layout.view.*
import timber.log.Timber
import javax.inject.Inject

interface RateWhiskyView : MvpView {
  fun loadFormIntent(): Observable<Unit>
  fun submitFormIntent(): Observable<Niceness>
  fun render(viewState: RateWhiskyViewState)
}

class RateWhiskyLayout(context: Context?, attrs: AttributeSet?)
  : MviFrameLayout<RateWhiskyView, RateWhiskyPresenter>(context, attrs), RateWhiskyView {

  @Inject lateinit var presenter: RateWhiskyPresenter

  init {
    context?.getDaggerComponent<RateWhiskyActivity.Component>()?.inject(this)
  }

  override fun createPresenter(): RateWhiskyPresenter {
    return presenter
  }

  override fun onFinishInflate() {
    super.onFinishInflate()

    val adapter = ArrayAdapter<Niceness>(context, android.R.layout.simple_spinner_item, Niceness.values())
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.rate_whisky_niceness.adapter = adapter
  }

  override fun loadFormIntent(): Observable<Unit> {
    return RxView.attachEvents(this).map { Unit }
  }

  override fun submitFormIntent(): Observable<Niceness> {
    return RxView.clicks(this.rate_whisky_submit)
      .map { this.rate_whisky_niceness.selectedItem as Niceness }
  }

  override fun render(viewState: RateWhiskyViewState) {
    Timber.e("RateWhiskyView: RENDERING ${viewState.javaClass.simpleName}")

    when (viewState) {
      RateWhiskyFormLoading -> {
        TransitionManager.beginDelayedTransition(this)
        this.rate_whisky_progress_bar.visibility = VISIBLE
        this.rate_whisky_form.visibility = GONE
      }

      is RateWhiskyFormError -> {
        TransitionManager.beginDelayedTransition(this)
        this.rate_whisky_progress_bar.visibility = GONE
        this.rate_whisky_form.visibility = GONE

        makeText(context, "Failed to load whisky: ${viewState.error.message}", LENGTH_LONG).show()
      }

      is RateWhiskyOpenForm -> {
        this.rate_whisky_progress_bar.visibility = GONE
        this.rate_whisky_form.visibility = VISIBLE
        this.rate_whisky_niceness.isEnabled = true
        this.rate_whisky_submit.isEnabled = true
        this.rate_whisky_name.text = viewState.whisky.name
        if (viewState.niceness != null) {
          this.rate_whisky_niceness.setSelection(Niceness.values().indexOf(viewState.niceness))
        }
      }

      RateWhiskyFormSubmitting -> {
        TransitionManager.beginDelayedTransition(this)
        this.rate_whisky_progress_bar.visibility = GONE
        this.rate_whisky_form.visibility = VISIBLE
        this.rate_whisky_niceness.isEnabled = false
        this.rate_whisky_submit.isEnabled = false
      }

      RateWhiskyFormSubmitSuccessful -> {
        makeText(context, "Rating submitted! VERY NICE!", LENGTH_LONG).show()
        context.asActivity()?.finish()
      }

      is RateWhiskyFormSubmitError -> {
        TransitionManager.beginDelayedTransition(this)
        this.rate_whisky_progress_bar.visibility = GONE
        this.rate_whisky_form.visibility = VISIBLE
        this.rate_whisky_niceness.isEnabled = true
        this.rate_whisky_submit.isEnabled = true

        makeText(context, "Failed to submit rating ${viewState.error.message}", LENGTH_LONG).show()
        context.asActivity()?.finish()
      }
    }
  }
}