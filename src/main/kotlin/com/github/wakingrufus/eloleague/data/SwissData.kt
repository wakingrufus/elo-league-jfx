package com.github.wakingrufus.eloleague.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


data class SwissTeamData(
        val id: String,
        val name: String,
        val playerIds: Set<String>)

data class SwissGameData(val id: String,
                         val gameId: String,
                         val winningTeamId: String)

data class SwissRound(val roundNumber: Int, val pairings: List<SwissPairingData>)

data class SwissPairingData(val id: String,
                            val teamIds: List<String>,
                            val games: List<SwissGameData>,
                            val drops: List<String> = ArrayList())

@JsonIgnoreProperties(ignoreUnknown = true)
data class SwissTournamentData(val id: String,
                               val startTime: Long,
                               val name: String,
                               val teams: List<SwissTeamData>,
                               val rounds: List<SwissRound>)