package com.balysv.verynicedrinks.infrastructure.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.balysv.verynicedrinks.infrastructure.db.entity.RandomFactEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface RandomFactDao {

  @Query("SELECT * FROM random_fact WHERE id = :id")
  fun find(id: Long): Maybe<RandomFactEntity>

  @Query("SELECT * FROM random_fact")
  fun findAll(): Single<List<RandomFactEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(randomFact: RandomFactEntity): Long
}