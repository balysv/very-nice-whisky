package com.balysv.verynicedrinks.infrastructure.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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