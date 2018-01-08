package com.balysv.verynicedrinks.domain.rating

data class Rating(
    val id: Long,
    val whiskyId: Long,
    val niceness: Niceness
)

enum class Niceness {
  NOT_NICE,
  NICEISH,
  QUITE_NICE,
  NICE,
  VERY_NICE,
  VERY_VERY_NICE
}