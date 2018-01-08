package com.balysv.verynicedrinks.presentation.whiskysearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.R.layout.whisky_search_activity
import com.balysv.verynicedrinks.VeryNiceApplication
import com.balysv.verynicedrinks.interactor.InteractorModule
import com.balysv.verynicedrinks.presentation.BaseActivity
import dagger.Subcomponent

class WhiskySearchActivity : BaseActivity<WhiskySearchActivity.Component>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(whisky_search_activity)
  }

  override fun buildActivityComponent(): Component {
    return VeryNiceApplication.appGraph.whiskySearchActivity()
      .useCaseModule(InteractorModule())
      .build()
  }

  @PerActivity
  @Subcomponent(modules = arrayOf(InteractorModule::class))
  interface Component {
    fun inject(layout: WhiskySearchLayout)

    @Subcomponent.Builder
    interface Builder {
      fun useCaseModule(interactorModule: InteractorModule): Builder
      fun build(): Component
    }
  }

  companion object {
    fun intent(context: Context): Intent {
      return Intent(context, WhiskySearchActivity::class.java)
    }
  }
}
