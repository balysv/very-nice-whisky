package com.balysv.verynicedrinks.domain.rating

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RatingRepository {
  fun findByWhiskyId(whiskyId: Long): Maybe<Rating>
  fun findAll(): Flowable<List<Rating>>
  fun add(rating: Rating): Single<Rating>
  fun remove(id: Long): Completable
  fun modify(id: Long, rating: Rating): Single<Rating>
}