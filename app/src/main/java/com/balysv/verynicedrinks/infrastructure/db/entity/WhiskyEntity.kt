package com.balysv.verynicedrinks.infrastructure.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier
import com.balysv.verynicedrinks.domain.whisky.WhiskyType

@Entity(tableName = "whisky")
data class WhiskyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name", index = true)
    var name: String,

    @ColumnInfo(name = "cost_tier")
    var costTier: WhiskyCostTier,

    @ColumnInfo(name = "type")
    var type: WhiskyType,

    @ColumnInfo(name = "country")
    var country: String
)