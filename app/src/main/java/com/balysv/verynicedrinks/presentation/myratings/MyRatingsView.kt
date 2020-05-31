package com.balysv.verynicedrinks.presentation.myratings

import android.content.Context
import androidx.transition.TransitionManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import com.balysv.verynicedrinks.R
import com.balysv.verynicedrinks.interactor.*
import com.balysv.verynicedrinks.getDaggerComponent
import com.balysv.verynicedrinks.inflate
import com.balysv.verynicedrinks.presentation.ratewhisky.RateWhiskyActivity
import com.balysv.verynicedrinks.presentation.whiskysearch.WhiskySearchActivity
import com.hannesdorfmann.mosby3.mvi.layout.MviRelativeLayout
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.my_ratings_item.view.*
import kotlinx.android.synthetic.main.my_ratings_layout.view.*
import timber.log.Timber
import javax.inject.Inject

interface MyRatingsView : MvpView {
  fun loadRatedWhiskiesIntent(): Observable<Unit>

  fun render(viewState: MyRatingsViewState)
}

class MyRatingsLayout(context: Context?, attrs: AttributeSet?)
  : MviRelativeLayout<MyRatingsView, MyRatingsPresenter>(context, attrs), MyRatingsView {

  @Inject
  lateinit var presenter: MyRatingsPresenter

  private val adapter = MyRatingsAdapter()

  init {
    context?.getDaggerComponent<MyRatingsActivity.Component>()?.inject(this)
  }

  override fun createPresenter(): MyRatingsPresenter {
    return presenter
  }

  override fun onFinishInflate() {
    super.onFinishInflate()

    RxView.clicks(this.my_ratings_fab)
      .subscribe({ context.startActivity(WhiskySearchActivity.intent(context)) })

    this.my_ratings_recycler_view.layoutManager = LinearLayoutManager(context)
    this.my_ratings_recycler_view.adapter = adapter
    adapter.onClickListener = {
      context.startActivity(RateWhiskyActivity.intent(context, it.whisky.id))
    }
  }

  override fun loadRatedWhiskiesIntent(): Observable<Unit> {
    return RxView.attachEvents(this).map { Unit }
  }

  override fun render(viewState: MyRatingsViewState) {
    Timber.e("MyRatingsView: RENDERING ${viewState.javaClass.simpleName}")

    when (viewState) {

      MyRatingsLoading -> {
        TransitionManager.beginDelayedTransition(this)
        this.my_ratings_progress_bar.visibility = VISIBLE
        this.my_ratings_recycler_view.visibility = GONE
        this.my_ratings_random_fact.visibility = GONE
      }

      is MyRatingsLoadingFact -> {
        TransitionManager.beginDelayedTransition(this)
        this.my_ratings_progress_bar.visibility = VISIBLE
        this.my_ratings_recycler_view.visibility = GONE
        this.my_ratings_random_fact.visibility = VISIBLE
        this.my_ratings_random_fact.text = viewState.randomFact.text
      }

      is MyRatingsLoaded -> {
        TransitionManager.beginDelayedTransition(this)
        this.my_ratings_progress_bar.visibility = GONE
        this.my_ratings_random_fact.visibility = GONE
        this.my_ratings_recycler_view.visibility = VISIBLE
        adapter.items = viewState.ratedWhiskies
        adapter.notifyDataSetChanged()
      }

      is MyRatingsError -> {
        TransitionManager.beginDelayedTransition(this)
        this.my_ratings_progress_bar.visibility = GONE
        this.my_ratings_recycler_view.visibility = GONE
        this.my_ratings_random_fact.visibility = GONE

        makeText(context, "Failed loading ratings ${viewState.error.message}", LENGTH_LONG).show()
      }
    }
  }

  private class MyRatingsAdapter : RecyclerView.Adapter<ViewHolder>() {
    lateinit var onClickListener: (RatedWhisky) -> Unit
    var items: List<RatedWhisky> = listOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(items[position], onClickListener)
    }

    override fun getItemCount(): Int {
      return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(parent.inflate(R.layout.my_ratings_item))
    }
  }

  private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: RatedWhisky, onClickListener: (RatedWhisky) -> Unit) = with(itemView) {
      itemView.my_ratings_item_name.text = item.whisky.name
      itemView.my_ratings_item_niceness.text = item.rating.niceness.toString()
      setOnClickListener { onClickListener(item) }
    }
  }
}