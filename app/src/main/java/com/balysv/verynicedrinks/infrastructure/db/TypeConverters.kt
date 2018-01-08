package com.balysv.verynicedrinks.infrastructure.db

import android.arch.persistence.room.TypeConverter
import com.balysv.verynicedrinks.domain.rating.Niceness
import com.balysv.verynicedrinks.domain.whisky.WhiskyCostTier
import com.balysv.verynicedrinks.domain.whisky.WhiskyType


class WhiskyTypeConverter {
  @TypeConverter
  fun fromTextToWhiskyType(whiskyType: String): WhiskyType {
    return WhiskyType.valueOf(whiskyType)
  }

  @TypeConverter
  fun fromWhiskyTypeToText(whiskyType: WhiskyType): String {
    return whiskyType.toString()
  }
}

class CostTierConverter {

  @TypeConverter
  fun fromTextToCostTier(costTier: String): WhiskyCostTier {
    return WhiskyCostTier.valueOf(costTier)
  }

  @TypeConverter
  fun fromCostTierToText(costTier: WhiskyCostTier): String {
    return costTier.toString()
  }
}

class NicenessConverter {

  @TypeConverter
  fun fromTextToCostTier(niceness: String): Niceness {
    return Niceness.valueOf(niceness)
  }

  @TypeConverter
  fun fromCostTierToText(niceness: Niceness): String {
    return niceness.toString()
  }
}