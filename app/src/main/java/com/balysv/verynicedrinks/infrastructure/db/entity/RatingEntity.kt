package com.balysv.verynicedrinks.infrastructure.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.balysv.verynicedrinks.domain.rating.Niceness


@Entity(
  tableName = "rating",
  indices = arrayOf(Index(value = ["whisky_id"], unique = true))
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