package com.balysv.verynicedrinks.infrastructure.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.balysv.verynicedrinks.infrastructure.db.dao.RandomFactDao
import com.balysv.verynicedrinks.infrastructure.db.dao.RatingDao
import com.balysv.verynicedrinks.infrastructure.db.dao.WhiskyDao
import com.balysv.verynicedrinks.infrastructure.db.entity.RandomFactEntity
import com.balysv.verynicedrinks.infrastructure.db.entity.RatingEntity
import com.balysv.verynicedrinks.infrastructure.db.entity.WhiskyEntity


@Database(entities = arrayOf(WhiskyEntity::class, RatingEntity::class, RandomFactEntity::class), version = 1)
@TypeConverters(WhiskyTypeConverter::class, CostTierConverter::class, NicenessConverter::class)
abstract class WhiskyDatabase : RoomDatabase() {
  abstract fun whiskyDao(): WhiskyDao
  abstract fun ratingDao(): RatingDao
  abstract fun randomFactDao(): RandomFactDao
}