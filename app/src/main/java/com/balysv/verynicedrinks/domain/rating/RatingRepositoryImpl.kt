package com.balysv.verynicedrinks.domain.rating

import com.balysv.verynicedrinks.infrastructure.db.dao.RatingDao
import com.balysv.verynicedrinks.infrastructure.db.entity.RatingEntity
import io.reactivex.*
import java.util.concurrent.TimeUnit

internal class RatingRepositoryImpl(private val ratingDao: RatingDao) : RatingRepository {

  private val apiMapper: (RatingEntity) -> Rating = { Rating(it.id, it.whiskyId, it.niceness) }

  override fun findByWhiskyId(whiskyId: Long): Maybe<Rating> {
    return ratingDao.findByWhiskyId(whiskyId)
      .map(apiMapper)
  }

  override fun findAll(): Flowable<List<Rating>> {
    return ratingDao.findAll()
      .map { it.map(apiMapper) }
  }

  override fun add(rating: Rating): Single<Rating> {
    return Single
      .fromCallable { ratingDao.insert(RatingEntity(0, rating.whiskyId, rating.niceness)) }
      .flatMap { ratingDao.find(it).toSingle() }
      .map(apiMapper)
      .delay(1, TimeUnit.SECONDS)
  }

  override fun remove(id: Long): Completable {
    return Completable.fromAction {
      ratingDao.delete(id)
    }
  }

  override fun modify(id: Long, rating: Rating): Single<Rating> {
    return ratingDao.find(id)
      .toSingle()
      .map { RatingEntity(it.id, rating.whiskyId, rating.niceness) }
      .doOnSuccess({ ratingDao.update(it) })
      .map(apiMapper)
      .delay(1, TimeUnit.SECONDS)
  }
}