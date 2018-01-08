package com.balysv.verynicedrinks.infrastructure.api

import com.balysv.verynicedrinks.infrastructure.api.entity.WhiskyRandomFactResults
import io.reactivex.Observable
import retrofit2.http.GET

interface WhiskyApi {

  @GET("randomfact/")
  fun getRandomFact(): Observable<WhiskyRandomFactResults>
}