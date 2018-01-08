package com.balysv.verynicedrinks.presentation.myratings

import android.os.Bundle
import com.balysv.verynicedrinks.PerActivity
import com.balysv.verynicedrinks.R
import com.balysv.verynicedrinks.VeryNiceApplication
import com.balysv.verynicedrinks.interactor.InteractorModule
import com.balysv.verynicedrinks.presentation.BaseActivity
import dagger.Subcomponent

class MyRatingsActivity : BaseActivity<MyRatingsActivity.Component>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.my_ratings_activity)
  }

  override fun buildActivityComponent(): Component {
    return VeryNiceApplication.appGraph.myRatingsActivity()
      .useCaseModule(InteractorModule())
      .build()
  }

  @PerActivity
  @Subcomponent(modules = arrayOf(InteractorModule::class))
  interface Component {
    fun inject(layout: MyRatingsLayout)

    @Subcomponent.Builder
    interface Builder {
      fun useCaseModule(interactorModule: InteractorModule): Builder
      fun build(): Component
    }
  }
}
