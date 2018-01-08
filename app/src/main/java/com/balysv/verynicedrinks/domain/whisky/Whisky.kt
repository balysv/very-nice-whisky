package com.balysv.verynicedrinks.domain.whisky

data class Whisky(
    val id: Long,
    val name: String,
    val costTier: WhiskyCostTier,
    val type: WhiskyType,
    val country: String
)

enum class WhiskyType {
  MALT,
  BLEND,
  GRAIN,
  BOURBON,
  RYE,
  WHEAT,
  FLAVOURED,
  BARLEY,
  WHISKEY,
  UNKNOWN
}

enum class WhiskyCostTier {
  // $ <$30 CAD
  FIRST,
  // $$ $30~$50 CAD
  SECOND,
  // $$$ $50-$70 CAD
  THIRD,
  // $$$$ $70~$125 CAD
  FOURTH,
  // $$$$$ $125~$300 CAD
  FIFTH,
  // $$$$$+ >$300 CAD.
  SIXTH,
  // ???
  UNKNOWN
}
