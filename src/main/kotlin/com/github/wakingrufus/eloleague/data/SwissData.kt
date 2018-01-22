package com.github.wakingrufus.eloleague.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


data class SwissTeamData(
        val id: String,
        val name: String,
        val playerIds: Set<String>)

data class SwissGameData(val id: String,
                         val gameId: String,
                         val winningTeam: String)

data class SwissRound(val results: List<SwissResultData>)

data class SwissResultData(val pairing: SwissPairing, val games: List<SwissGameData>)

data class SwissPairing(val id: String,
                        val teamIds: Pair<String, String>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SwissTournamentData(val id: String,
                               val startTime: Long,
                               val name: String,
                               val teams: List<SwissTeamData>,
                               val rounds: Map<Int, SwissRound>)