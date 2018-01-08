package com.balysv.verynicedrinks

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import android.content.ContextWrapper
import android.app.Activity


const val DAGGER_SERVICE_NAME: String = "DaggerService"

@Suppress("UNCHECKED_CAST")
@SuppressLint("WrongConstant")
fun <T> Context.getDaggerComponent(): T {
  return getSystemService(DAGGER_SERVICE_NAME) as T
}

fun <T> Observable<T>.bindAsyncSchedulers(): Observable<T> {
  return subscribeOn(io()).observeOn(mainThread())
}

fun <T> Flowable<T>.bindAsyncSchedulers(): Flowable<T> {
  return subscribeOn(io()).observeOn(mainThread())
}

fun <T> Single<T>.bindAsyncSchedulers(): Single<T> {
  return subscribeOn(io()).observeOn(mainThread())
}

fun <T> Maybe<T>.bindAsyncSchedulers(): Maybe<T> {
  return subscribeOn(io()).observeOn(mainThread())
}

fun ViewGroup.inflate(layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Context.asActivity(): Activity? {
  var context = this
  while (context is ContextWrapper) {
    if (context is Activity) {
      return context
    }
    context = context.baseContext
  }
  return null
}

