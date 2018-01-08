package com.balysv.verynicedrinks.infrastructure

import com.balysv.verynicedrinks.infrastructure.api.WhiskyApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ApiModule {

  @Provides
  @Singleton
  fun apiUrl() = "https://evening-citadel-85778.herokuapp.com/"

  @Provides
  @Singleton
  fun retrofit(apiUrl: String) =
    Retrofit.Builder()
      .baseUrl(apiUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()

  @Provides
  @Singleton
  fun whiskyApi(retrofit: Retrofit) = retrofit.create(WhiskyApi::class.java)
}