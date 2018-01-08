package com.balysv.verynicedrinks.domain.randomfact

import com.balysv.verynicedrinks.infrastructure.api.WhiskyApi
import com.balysv.verynicedrinks.infrastructure.db.dao.RandomFactDao
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class RandomFactRepositoryScope

@RandomFactRepositoryScope
@Subcomponent(modules = arrayOf(RandomFactRepositoryModule::class))
interface RandomFactRepositoryComponent {
  @RandomFactRepositoryScope
  fun randomFactRepository(): RandomFactRepository

  @Subcomponent.Builder
  interface Builder {
    fun module(module: RandomFactRepositoryModule): Builder
    fun build(): RandomFactRepositoryComponent
  }
}

@Module
class RandomFactRepositoryModule {
  @Provides
  @RandomFactRepositoryScope
  fun randomFactRepositoryImpl(dao: RandomFactDao, api: WhiskyApi): RandomFactRepository
    = RandomFactRepositoryImpl(dao, api)
}