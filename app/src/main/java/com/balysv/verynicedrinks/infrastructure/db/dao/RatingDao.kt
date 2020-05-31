package com.balysv.verynicedrinks.infrastructure.db.dao

import androidx.room.*
import com.balysv.verynicedrinks.infrastructure.db.entity.RatingEntity
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface RatingDao {

  @Query("SELECT * FROM rating WHERE whisky_id = :whiskyId")
  fun findByWhiskyId(whiskyId: Long): Maybe<RatingEntity>

  @Query("SELECT * FROM rating WHERE id = :id")
  fun find(id: Long): Maybe<RatingEntity>

  @Query("SELECT * FROM rating")
  fun findAll(): Flowable<List<RatingEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(rating: RatingEntity): Long

  @Update
  fun update(rating: RatingEntity): Int

  @Query("DELETE FROM rating WHERE id = :id")
  fun delete(id: Long)
}