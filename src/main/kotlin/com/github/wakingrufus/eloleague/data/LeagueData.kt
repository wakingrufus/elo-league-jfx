package com.github.wakingrufus.eloleague.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class LeagueData(
        val id: String,
        val name: String = "",
        val players: Set<PlayerData> = HashSet(),
        var games: List<GameData> = ArrayList(),
        var startingRating: Int = 1500,
        var xi: Int = 1000,
        var kFactorBase: Int = 32,
        var trialPeriod: Int = 10,
        var trialKFactorMultiplier: Int = 2
)