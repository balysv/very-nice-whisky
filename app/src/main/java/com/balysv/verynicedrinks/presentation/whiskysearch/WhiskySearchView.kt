package com.balysv.verynicedrinks.presentation.whiskysearch

import android.content.Context
import androidx.transition.TransitionManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.balysv.verynicedrinks.R
import com.balysv.verynicedrinks.interactor.*
import com.balysv.verynicedrinks.getDaggerComponent
import com.balysv.verynicedrinks.inflate
import com.balysv.verynicedrinks.domain.whisky.Whisky
import com.balysv.verynicedrinks.presentation.ratewhisky.RateWhiskyActivity
import com.hannesdorfmann.mosby3.mvi.layout.MviRelativeLayout
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.whisky_search_item.view.*
import kotlinx.android.synthetic.main.whisky_search_layout.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface WhiskySearchView : MvpView {
  fun searchIntent(): Observable<CharSequence>
  fun render(viewState: WhiskySearchViewState)
}

class WhiskySearchLayout(context: Context?, attrs: AttributeSet?)
  : MviRelativeLayout<WhiskySearchView, WhiskySearchPresenter>(context, attrs), WhiskySearchView {

  @Inject lateinit var presenter: WhiskySearchPresenter

  private val adapter = WhiskySearchAdapter()

  init {
    context?.getDaggerComponent<WhiskySearchActivity.Component>()?.inject(this)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()

    this.whisky_search_search_view.setIconifiedByDefault(false)
    this.whisky_search_recycler_view.layoutManager = LinearLayoutManager(context)
    this.whisky_search_recycler_view.adapter = adapter
    adapter.onClickListener = {
      context.startActivity(RateWhiskyActivity.intent(context, it.id))
    }
  }

  override fun createPresenter(): WhiskySearchPresenter {
    return presenter
  }

  override fun searchIntent(): Observable<CharSequence> {
    return RxSearchView.queryTextChanges(this.whisky_search_search_view)
      .debounce(500, TimeUnit.MILLISECONDS)
  }

  override fun render(viewState: WhiskySearchViewState) {
    Timber.e("WhiskySearchView: RENDERING ${viewState.javaClass.simpleName}")

    when (viewState) {

      WhiskySearchNotStarted -> {
        TransitionManager.beginDelayedTransition(this)
        this.whisky_search_recycler_view.visibility = View.GONE
        this.whisky_search_progress_bar.visibility = View.GONE
        this.whisky_search_empty_view.visibility = View.VISIBLE
        this.whisky_search_empty_view.text = "Enter 3 or more characters"
      }

      WhiskySearchLoading -> {
        TransitionManager.beginDelayedTransition(this)
        this.whisky_search_recycler_view.visibility = View.GONE
        this.whisky_search_progress_bar.visibility = View.VISIBLE
        this.whisky_search_empty_view.visibility = View.GONE
      }

      is WhiskySearchEmpty -> {
        TransitionManager.beginDelayedTransition(this)
        this.whisky_search_recycler_view.visibility = View.GONE
        this.whisky_search_progress_bar.visibility = View.GONE
        this.whisky_search_empty_view.setText("NO RESULTS :(")
        this.whisky_search_empty_view.visibility = View.VISIBLE
      }

      is WhiskySearchResults -> {
        TransitionManager.beginDelayedTransition(this)
        this.whisky_search_recycler_view.visibility = View.VISIBLE
        adapter.items = viewState.results
        adapter.notifyDataSetChanged()

        this.whisky_search_progress_bar.visibility = View.GONE
        this.whisky_search_empty_view.visibility = View.GONE
      }

      is WhiskySearchError -> {
        TransitionManager.beginDelayedTransition(this)
        this.whisky_search_recycler_view.visibility = View.GONE
        this.whisky_search_progress_bar.visibility = View.GONE
        this.whisky_search_empty_view.setText("ERROR ERROR!")
        this.whisky_search_empty_view.visibility = View.VISIBLE
      }
    }
  }


  private class WhiskySearchAdapter
    : RecyclerView.Adapter<ViewHolder>() {

    lateinit var onClickListener: (Whisky) -> Unit
    var items: List<Whisky> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(items[position], onClickListener)
    }

    override fun getItemCount(): Int {
      return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(parent.inflate(R.layout.whisky_search_item))
    }
  }

  private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Whisky, onClickListener: (Whisky) -> Unit) = with(itemView) {
      itemView.whisky_search_item_name.text = item.name
      setOnClickListener { onClickListener(item) }
    }
  }
}
