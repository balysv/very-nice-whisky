package com.balysv.verynicedrinks.domain.whisky

import com.balysv.verynicedrinks.infrastructure.db.dao.WhiskyDao
import com.balysv.verynicedrinks.infrastructure.db.entity.WhiskyEntity
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.*
import io.reactivex.rxkotlin.toObservable
import java.util.concurrent.TimeUnit

internal class WhiskyRepositoryImpl(
  private val dao: WhiskyDao,
  private val whiskyDatabaseLoaded: Preference<Boolean>
) : WhiskyRepository {

  private val apiMapper: (WhiskyEntity) -> Whisky
    = { Whisky(it.id, it.name, it.costTier, it.type, it.country) }

  override fun find(id: Long): Maybe<Whisky> {
    return dao.find(id)
      .map(apiMapper)
      .delay(500, TimeUnit.MILLISECONDS)
  }

  override fun findAll(ids: Collection<Long>): Flowable<List<Whisky>> {
    return dao.findAll(ids.toList())
      .map { it.map(apiMapper) }
  }

  override fun search(query: String): Flowable<List<Whisky>> {
    return dao.search("%${query.toLowerCase()}%")
      .map( { it.map(apiMapper)})
      .delay(1, TimeUnit.SECONDS)
  }

  override fun addAll(whiskies: Collection<Whisky>): Completable {
    return whiskies.toObservable()
      .map { WhiskyEntity(0, it.name, it.costTier, it.type, it.country) }
      .toList()
      .doOnSuccess({
        dao.save(it)
        whiskyDatabaseLoaded.set(true)
      })
      .toCompletable()
  }

  override fun isEmpty(): Single<Boolean> {
    return Single.just(!whiskyDatabaseLoaded.get())
  }
}