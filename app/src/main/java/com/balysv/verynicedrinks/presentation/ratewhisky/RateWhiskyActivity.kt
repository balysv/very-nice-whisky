package com.balysv.verynicedrinks.presentation.ratewhisky

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.R.layout.rate_whisky_activity
import com.balysv.verynicedrinks.VeryNiceApplication
import com.balysv.verynicedrinks.interactor.InteractorModule
import com.balysv.verynicedrinks.presentation.BaseActivity
import dagger.Provides
import dagger.Subcomponent


class RateWhiskyActivity : BaseActivity<RateWhiskyActivity.Component>() {

  private var whiskyId: Long? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    whiskyId = intent.getLongExtra(WHISKY_ID, -1)
    if (whiskyId!! == -1L) throw IllegalArgumentException("Invalid whiskyId")

    setContentView(rate_whisky_activity)
  }

  override fun buildActivityComponent(): Component {
    return VeryNiceApplication.appGraph.rateWhiskyActivity()
      .useCaseModule(InteractorModule())
      .activityModule(Module(whiskyId!!))
      .build()
  }

  @dagger.Module
  class Module(val whiskyId: Long) {

    @Provides
    fun whiskyId() = whiskyId
  }

  @PerActivity
  @Subcomponent(modules = arrayOf(Module::class, InteractorModule::class))
  interface Component {
    fun inject(layout: RateWhiskyLayout)

    @Subcomponent.Builder
    interface Builder {
      fun useCaseModule(interactorModule: InteractorModule): Builder
      fun activityModule(module: Module): Builder
      fun build(): Component
    }
  }

  companion object {
    private const val WHISKY_ID = "whisky_id"

    fun intent(context: Context, whiskyId: Long): Intent {
      return Intent(context, RateWhiskyActivity::class.java).putExtra(WHISKY_ID, whiskyId)
    }
  }
}
