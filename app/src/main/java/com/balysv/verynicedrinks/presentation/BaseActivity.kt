package com.balysv.verynicedrinks.presentation

import androidx.appcompat.app.AppCompatActivity
import com.balysv.verynicedrinks.DAGGER_SERVICE_NAME
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle


abstract class BaseActivity<out T> : AppCompatActivity() {
  internal val lifecycle = AndroidLifecycle.createLifecycleProvider(this)

  val activityGraph: T by lazy {
    buildActivityComponent()!!
  }

  protected abstract fun buildActivityComponent(): T?

  override fun getSystemService(name: String): Any? {
    if (application == null) {
      return super.getSystemService(name)
    }

    if (name == DAGGER_SERVICE_NAME) {
      return activityGraph
    }

    return super.getSystemService(name)
  }
}