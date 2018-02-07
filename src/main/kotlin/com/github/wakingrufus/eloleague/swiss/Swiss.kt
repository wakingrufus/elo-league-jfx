package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.game.GameItem
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

fun totalRounds(teams: Int): Int {
    return teams.toDouble().pow(2).roundToInt()
}


fun randomPairing(teams: List<SwissTeamItem>): List<SwissPairingItem> {
    return teams.take(teams.size / 2).toList().shuffled()
            .zip(teams.takeLast(teams.size / 2).shuffled())
            .map {
                SwissPairingItem(id = UUID.randomUUID().toString()).apply {
                    teamsProperty.setAll(listOf(it.first, it.second))
                }
            }.toList()
}

fun pairing(records: List<SwissStanding>, previousPairings: List<SwissPairingItem>): List<SwissPairingItem> {
    val playersToPair = mutableListOf<SwissStanding>()
    playersToPair.addAll(records.sortedWith(standingSorter))
    return records.sortedWith(standingSorter).zipWithNext { first, second ->
        if (playersToPair.any { it.team.id == first.team.id }) {
            SwissPairingItem(id = UUID.randomUUID().toString()
            ).apply {
                this.teamsProperty.addAll(first.team, second.team)
                playersToPair.remove(first)
                playersToPair.remove(second)
            }
        } else {
            null
        }
    }.filterNotNull()

}

fun gameMatchesPairing(game: GameItem, pairing: SwissPairingItem): Boolean =
        pairing.teamsProperty
                .all { team ->
                    listOf(game.team1Players, game.team2Players)
                            .any { team.players.map { it.id }.containsAll(it.map { it.id }) }
                }

fun calculateCurrentStandings(tournament: SwissTournamentItem): List<SwissStanding> {
    return tournament.rounds.fold(
            initial = tournament.teams.map { SwissStanding(it) },
            operation = ::addRoundToStandings)
            .let(::calculateOpponentStats)
}

fun calculateOpponentStats(standings: List<SwissStanding>): List<SwissStanding> =
        standings.map { standing ->
            standing.copy(
                    opponentGameWinPct = standing.opponents
                            .map { opponentId ->
                                standings.first { it.team.id == opponentId }
                            }.map { arrayOf(gameWinPct(it), .33).max() as Double }.average(),
                    opponentMatchWinPct = standing.opponents
                            .map { opponentId ->
                                standings.first { it.team.id == opponentId }
                            }.map { arrayOf(matchWinPct(it), .33).max() as Double }.average())
        }


fun addRoundToStandings(oldStandings: List<SwissStanding>, round: SwissRoundItem): List<SwissStanding> =
        round.pairingsProperty.flatMap { pairing ->
            pairing.teamsProperty.map { teamItem ->
                oldStandings.first { it.team.id == teamItem.id }.let {
                    it.copy(
                            gamePoints = it.gamePoints + pairing.gamePoints(teamItem.id),
                            totalGames = it.totalGames + pairing.gamesProperty.size,
                            matchPoints = it.matchPoints + matchPoints(pairing = pairing, teamId = teamItem.id),
                            totalMatches = it.totalMatches + 1,
                            opponents = it.opponents.plus(pairing.teamsProperty.map { it.id }.minus(teamItem.id))
                    )
                }
            }
        }

fun matchPoints(pairing: SwissPairingItem, teamId: String): Int = when {
    pairing.getWinningTeam() == null -> 1
    pairing.getWinningTeam() == teamId -> 3
    else -> 0
}

fun gameWinPct(standing: SwissStanding): Double =
        standing.gamePoints.toDouble() / standing.totalGames.toDouble().times(3)


fun matchWinPct(standing: SwissStanding): Double =
        standing.matchPoints.toDouble() / (standing.totalMatches.toDouble().times(3))


val standingSorter: Comparator<SwissStanding> =
        Comparator.comparingInt(SwissStanding::matchPoints)
                .then(Comparator.comparingDouble(SwissStanding::opponentMatchWinPct))
                .then(Comparator.comparingDouble(::gameWinPct))
                .then(Comparator.comparingDouble(SwissStanding::opponentGameWinPct)).reversed()