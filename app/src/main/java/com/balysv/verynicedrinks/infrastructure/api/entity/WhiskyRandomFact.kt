package com.balysv.verynicedrinks.infrastructure.api.entity

data class WhiskyRandomFactResults(
  val count: Int,
  val results: List<WhiskyRandomFact>
)

data class WhiskyRandomFact(
  val id: Long,
  val text: String
)