package com.balysv.verynicedrinks.infrastructure.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.balysv.verynicedrinks.domain.rating.Niceness


@Entity(
  tableName = "rating",
  indices = arrayOf(Index(value = "whisky_id", unique = true))
)
data class RatingEntity(

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Long,

  @ColumnInfo(name = "whisky_id")
  var whiskyId: Long,

  @ColumnInfo(name = "niceness")
  var niceness: Niceness
)