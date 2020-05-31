package com.balysv.verynicedrinks.presentation.launcher

import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.R.layout.launcher_activity
import com.balysv.verynicedrinks.VeryNiceApplication
import com.balysv.verynicedrinks.bindAsyncSchedulers
import com.balysv.verynicedrinks.interactor.*
import com.balysv.verynicedrinks.presentation.BaseActivity
import com.balysv.verynicedrinks.presentation.myratings.MyRatingsActivity
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import dagger.Subcomponent
import kotlinx.android.synthetic.main.launcher_activity.*
import timber.log.Timber
import javax.inject.Inject

class LauncherActivity : BaseActivity<LauncherActivity.Component>() {

  @Inject lateinit var preloadWhiskyInteractor: PreloadWhiskyInteractor

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(launcher_activity)

    activityGraph.inject(this)

    preloadWhiskyInteractor.preload()
      .bindAsyncSchedulers()
      .bindUntilEvent(lifecycle, ON_DESTROY)
      .subscribe({
        when (it) {
          is PreloadWhiskyLoading -> {
            launcher_progress_bar.visibility = VISIBLE
          }
          is PreloadWhiskyComplete -> {
            launcher_progress_bar.visibility = INVISIBLE
            startActivity(Intent(this@LauncherActivity, MyRatingsActivity::class.java))
            finish()
          }
          is PreloadWhiskyError -> {
            launcher_progress_bar.visibility = INVISIBLE
            Timber.e(it.error, "Failed to preload whiskies")
          }
        }
      })
  }

  override fun buildActivityComponent(): Component? {
    return VeryNiceApplication.appGraph.launcherActivity()
      .useCaseModule(InteractorModule())
      .build()
  }

  @PerActivity
  @Subcomponent(modules = arrayOf(InteractorModule::class))
  interface Component {
    fun inject(activity: LauncherActivity)

    @Subcomponent.Builder
    interface Builder {
      fun useCaseModule(interactorModule: InteractorModule): Builder
      fun build(): Component
    }
  }
}
