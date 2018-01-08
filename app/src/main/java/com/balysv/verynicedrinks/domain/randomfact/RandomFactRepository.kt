package com.balysv.verynicedrinks.domain.randomfact

import io.reactivex.Single

interface RandomFactRepository {
  fun findAny(): Single<RandomFact>
}