package com.balysv.verynicedrinks.infrastructure

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.balysv.verynicedrinks.ForApplication
import com.balysv.verynicedrinks.infrastructure.db.WhiskyDatabase
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

  @Provides
  @Singleton
  fun whiskyDatabase(@ForApplication app: Context) =
    Room.databaseBuilder(app, WhiskyDatabase::class.java, "Whisky.db").build()

  @Provides
  @Singleton
  fun ratingDao(db: WhiskyDatabase) = db.ratingDao()

  @Provides
  @Singleton
  fun whiskyDao(db: WhiskyDatabase) = db.whiskyDao()

  @Provides
  @Singleton
  fun randomFactDao(db: WhiskyDatabase) = db.randomFactDao()

  @Provides
  @Singleton
  fun sharedPreferences(@ForApplication app: Context): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(app)

  @Provides
  @Singleton
  fun rxSharedPreferences(sharedPreferences: SharedPreferences) =
    RxSharedPreferences.create(sharedPreferences)
}