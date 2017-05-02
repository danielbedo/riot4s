package com.danielbedo.riot4s.service.statsv13.model


case class PlayerStatsSummaryListDto(playerStatSummaries: List[PlayerStatsSummaryDto])
case class PlayerStatsSummaryDto(
                                wins: Option[Int],
                                modifyDate: Long,
                                playerStatSummaryType: String,
                                losses: Option[Int]
                                )