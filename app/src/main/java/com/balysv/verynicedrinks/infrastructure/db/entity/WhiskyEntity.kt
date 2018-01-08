package com.balysv.verynicedrinks.infrastructure.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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