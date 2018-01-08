package com.balysv.verynicedrinks.domain.randomfact

import com.balysv.verynicedrinks.infrastructure.api.WhiskyApi
import com.balysv.verynicedrinks.infrastructure.db.dao.RandomFactDao
import com.balysv.verynicedrinks.infrastructure.db.entity.RandomFactEntity
import io.reactivex.Single
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class RandomFactRepositoryImpl(private val dao: RandomFactDao, private val api: WhiskyApi) : RandomFactRepository {

  override fun findAny(): Single<RandomFact> {
    val persistedFactObservable = dao.findAll()
      .filter { it.isNotEmpty() }
      .map { it[Random().nextInt(it.size)] }
      .map { RandomFact(it.id, it.text) }
      .toSingle()

    return api.getRandomFact()
      .timeout(2, TimeUnit.SECONDS)
      .map { it.results[0] }
      .doOnNext({ dao.insert(RandomFactEntity(it.id, it.text)) })
      .map { RandomFact(it.id, it.text) }
      .singleOrError()
      .onErrorResumeNext({
        when (it) {
          is TimeoutException -> persistedFactObservable
          is UnknownHostException -> persistedFactObservable
          else -> throw it
        }
      })
  }
}