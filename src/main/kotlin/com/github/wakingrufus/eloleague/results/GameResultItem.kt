package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.eloleague.player.PlayerItem

data class GameResultItem(val id: String,
                          val player: PlayerItem,
                          val ratingAdjustment: Int,
                          val startingRating: Int,
                          val win: Boolean,
                          val team1Score: Int,
                          val team2Score: Int,
                          val entryDate: String,
                          val team1Players: List<PlayerItem>,
                          val team2Players: List<PlayerItem>
)