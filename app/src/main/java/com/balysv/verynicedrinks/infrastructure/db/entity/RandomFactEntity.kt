package com.balysv.verynicedrinks.infrastructure.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "random_fact")
data class RandomFactEntity(

  @PrimaryKey
  @ColumnInfo(name = "id")
  var id: Long,

  @ColumnInfo(name = "text")
  var text: String
)