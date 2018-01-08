package com.balysv.verynicedrinks.infrastructure.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.balysv.verynicedrinks.infrastructure.db.entity.WhiskyEntity
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface WhiskyDao {

  @Query("SELECT * FROM whisky WHERE id = :id")
  fun find(id: Long): Maybe<WhiskyEntity>

  @Query("SELECT * FROM whisky WHERE id in(:ids)")
  fun findAll(ids: List<Long>): Flowable<List<WhiskyEntity>>

  @Query("SELECT * FROM whisky WHERE lower(name) like :name")
  fun search(name: String): Flowable<List<WhiskyEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun save(whisky: WhiskyEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun save(whiskies: List<WhiskyEntity>)
}