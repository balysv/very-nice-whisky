package com.balysv.verynicedrinks.domain.whisky

import com.balysv.verynicedrinks.infrastructure.db.dao.WhiskyDao
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class WhiskyRepositoryScope

@WhiskyRepositoryScope
@Subcomponent(modules = arrayOf(WhiskyRepositoryModule::class))
interface WhiskyRepositoryComponent {
  @WhiskyRepositoryScope
  fun whiskyRepository(): WhiskyRepository

  @Subcomponent.Builder
  interface Builder {
    fun module(module: WhiskyRepositoryModule): Builder
    fun build(): WhiskyRepositoryComponent
  }
}

@Module
class WhiskyRepositoryModule {

  @Provides
  fun isWhiskyDatabaseLoaded(prefs: RxSharedPreferences): Preference<Boolean> =
    prefs.getBoolean("whisky_db_loaded", false)

  @Provides
  @WhiskyRepositoryScope
  fun whiskyRepositoryImpl(dao: WhiskyDao, isDbLoaded: Preference<Boolean>): WhiskyRepository
    = WhiskyRepositoryImpl(dao, isDbLoaded)
}
