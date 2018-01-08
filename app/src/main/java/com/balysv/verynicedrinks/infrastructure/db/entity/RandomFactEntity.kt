package com.balysv.verynicedrinks.infrastructure.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "random_fact")
data class RandomFactEntity(

  @PrimaryKey
  @ColumnInfo(name = "id")
  var id: Long,

  @ColumnInfo(name = "text")
  var text: String
)