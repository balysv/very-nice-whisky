package com.balysv.verynicedrinks.domain.whisky

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface WhiskyRepository {
  fun find(id: Long): Maybe<Whisky>
  fun findAll(ids: Collection<Long>): Flowable<List<Whisky>>
  fun search(query: String): Flowable<List<Whisky>>
  fun addAll(whiskies: Collection<Whisky>): Completable
  fun isEmpty(): Single<Boolean>
}