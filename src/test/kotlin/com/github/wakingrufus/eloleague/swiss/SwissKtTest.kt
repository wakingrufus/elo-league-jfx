package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.game.GameItem
import com.github.wakingrufus.eloleague.player.PlayerItem
import mu.KLogging
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

typealias Assertion<T> = (Pair<T, T>) -> Unit

class SwissKtTest {
    companion object : KLogging()

    @Test
    fun `test gameMatchesPairing`() {
        var inputPlayers = listOf<PlayerItem>()
        for (i in 0..5) {
            inputPlayers += PlayerItem(id = UUID.randomUUID().toString())
        }

        val pairing = SwissPairingItem(id = UUID.randomUUID().toString()).apply {
            teamsProperty.setAll(
                    SwissTeamItem().apply { this.players.setAll(inputPlayers[0], inputPlayers[1]) },
                    SwissTeamItem().apply { this.players.setAll(inputPlayers[2], inputPlayers[3]) }
            )
        }

        assertTrue(gameMatchesPairing(
                game = game(listOf(inputPlayers[0], inputPlayers[1]), listOf(inputPlayers[2], inputPlayers[3])),
                pairing = pairing
        ))
        assertTrue("teams in different order", gameMatchesPairing(
                game = game(listOf(inputPlayers[2], inputPlayers[3]), listOf(inputPlayers[0], inputPlayers[1])),
                pairing = pairing
        ))
        assertTrue("players in different order", gameMatchesPairing(
                game = game(listOf(inputPlayers[1], inputPlayers[0]), listOf(inputPlayers[3], inputPlayers[2])),
                pairing = pairing
        ))
        kotlin.test.assertFalse(gameMatchesPairing(
                game = game(listOf(inputPlayers[0], inputPlayers[2]), listOf(inputPlayers[1], inputPlayers[3])),
                pairing = pairing
        ))
        kotlin.test.assertFalse(gameMatchesPairing(
                game = game(listOf(inputPlayers[4], inputPlayers[5]), listOf(inputPlayers[2], inputPlayers[3])),
                pairing = pairing),
                "wrong team"
        )
    }

    @Test
    fun `test getCurrentStandings`() {
        val teams = createTeams(4)
        //   val team1 =
        val expectedStandings = listOf(
                SwissStanding(team = teams[0], matchPoints = 6, gamePoints = 6, totalGames = 2, totalMatches = 2,
                        opponents = listOf(teams[1].id, teams[2].id), opponentMatchWinPct = .5, opponentGameWinPct = .5),
                SwissStanding(team = teams[2], matchPoints = 3, gamePoints = 3, totalGames = 2, totalMatches = 2,
                        opponents = listOf(teams[3].id, teams[0].id), opponentMatchWinPct = .665, opponentGameWinPct = .665),
                SwissStanding(team = teams[1], matchPoints = 3, gamePoints = 3, totalGames = 2, totalMatches = 2,
                        opponents = listOf(teams[0].id, teams[3].id), opponentMatchWinPct = .665, opponentGameWinPct = .665),
                SwissStanding(team = teams[3], matchPoints = 0, gamePoints = 0, totalGames = 2, totalMatches = 2,
                        opponents = listOf(teams[2].id, teams[1].id), opponentMatchWinPct = .5, opponentGameWinPct = .5)
        )

        val tournament = SwissTournamentItem(
                id = UUID.randomUUID().toString()
        ).apply {
            teamsProperty.setAll(teams)
            rounds.setAll(
                    SwissRoundItem(roundNumber = 1).apply {
                        pairingsProperty.setAll(
                                pairing(winningTeam = teams[0], losingTeam = teams[1]),
                                pairing(winningTeam = teams[2], losingTeam = teams[3])
                        )
                    },
                    SwissRoundItem(roundNumber = 2).apply {
                        pairingsProperty.setAll(
                                pairing(winningTeam = teams[0], losingTeam = teams[2]),
                                pairing(winningTeam = teams[1], losingTeam = teams[3])
                        )
                    }
            )
        }

        assertStandings(expected = expectedStandings,
                actual = calculateCurrentStandings(tournament).sortedWith(standingSorter))
    }

    @Test
    fun `test addRoundToStandings`() {
        val teams = createTeams(6)
        val expected = listOf(
                SwissStanding(team = teams[0], matchPoints = 3, gamePoints = 6, totalGames = 2, totalMatches = 1,
                        opponents = listOf(teams[1].id), opponentMatchWinPct = .33, opponentGameWinPct = .33),
                SwissStanding(team = teams[1], matchPoints = 0, gamePoints = 0, totalGames = 2, totalMatches = 1,
                        opponents = listOf(teams[0].id), opponentGameWinPct = 1.0, opponentMatchWinPct = 1.0),
                SwissStanding(team = teams[2], matchPoints = 1, gamePoints = 1, totalGames = 1, totalMatches = 1,
                        opponents = listOf(teams[3].id), opponentMatchWinPct = .33, opponentGameWinPct = .33),
                SwissStanding(team = teams[3], matchPoints = 1, gamePoints = 1, totalGames = 1, totalMatches = 1,
                        opponents = listOf(teams[2].id), opponentMatchWinPct = .33, opponentGameWinPct = .33),
                SwissStanding(team = teams[4], matchPoints = 3, gamePoints = 6, totalGames = 3, totalMatches = 1,
                        opponents = listOf(teams[5].id), opponentMatchWinPct = 0.0, opponentGameWinPct = .33),
                SwissStanding(team = teams[5], matchPoints = 0, gamePoints = 3, totalGames = 3, totalMatches = 1,
                        opponents = listOf(teams[4].id), opponentGameWinPct = .66, opponentMatchWinPct = 1.0)
        )
        val inputRound = SwissRoundItem(roundNumber = 1).apply {
            pairingsProperty.setAll(
                    pairing(winningTeam = teams[0], losingTeam = teams[1], winningTeamGameWins = 2),
                    pairingTie(Pair(first = teams[2], second = teams[3])),
                    pairing(winningTeam = teams[4], losingTeam = teams[5], winningTeamGameWins = 2, losingTeamGameWins = 1)
            )
        }

        val actual = addRoundToStandings(
                oldStandings = teams.map { SwissStanding(team = it) },
                round = inputRound)

        assertStandings(expected = expected, actual = actual, assertions = *arrayOf(assertIdName,
                assertMatchPoints,
                assertGamePoints,
                assertTotalMatches,
                assertTotalGames
        ))

    }


    private val assertIdName: Assertion<SwissStanding> = {
        assertEquals(message = "correct team: expected ${it.first.team.name} but was ${it.second.team.name}",
                expected = it.first.team.id, actual = it.second.team.id)
    }
    private val assertMatchPoints: Assertion<SwissStanding> = {
        assertEquals(message = "correct matchPoints for " + it.first.team.name,
                expected = it.first.matchPoints, actual = it.second.matchPoints)
    }
    private val assertGamePoints: Assertion<SwissStanding> = {
        assertEquals(message = "correct gamePoints for " + it.first.team.name,
                expected = it.first.gamePoints, actual = it.second.gamePoints)
    }
    private val assertTotalGames: Assertion<SwissStanding> = {
        assertEquals(message = "correct totalGames for " + it.first.team.name,
                expected = it.first.totalGames, actual = it.second.totalGames)
    }
    private val assertTotalMatches: Assertion<SwissStanding> = {
        assertEquals(message = "correct totalMatches for " + it.first.team.name,
                expected = it.first.totalMatches, actual = it.second.totalMatches)
    }
    private val assertOpponents: Assertion<SwissStanding> = {
        assertEquals(message = "correct opponents for " + it.first.team.name,
                expected = it.first.opponents, actual = it.second.opponents)
    }
    private val assertMatchWinPct: Assertion<SwissStanding> = {
        assertEquals(message = "correct opponentMatchWinPct for " + it.first.team.name,
                expected = it.first.opponentMatchWinPct, actual = it.second.opponentMatchWinPct)
    }
    private val assertGameWinPct: Assertion<SwissStanding> = {
        assertEquals(message = "correct opponentGameWinPct for " + it.first.team.name,
                expected = it.first.opponentGameWinPct, actual = it.second.opponentGameWinPct)
    }

    private fun assertStandings(expected: List<SwissStanding>,
                                actual: List<SwissStanding>,
                                vararg assertions: Assertion<SwissStanding> = arrayOf(
                                        assertIdName,
                                        assertMatchPoints,
                                        assertGamePoints,
                                        assertTotalGames,
                                        assertTotalMatches,
                                        assertOpponents,
                                        assertMatchWinPct,
                                        assertGameWinPct)) {
        assertEquals(message = "correct number of records", expected = expected.size, actual = actual.size)
        expected.zip(actual).forEach { pair ->
            assertions.forEach { it(pair) }
        }
    }

    @Test
    fun `test opponentstats calc`() {

    }

    @Test
    fun `test standings sorter`() {

        val teams = createTeams(5)
        val expected = listOf(
                SwissStanding(team = teams[0], matchPoints = 9, gamePoints = 18, totalGames = 6, totalMatches = 3,
                        opponentMatchWinPct = .66, opponentGameWinPct = .66),
                SwissStanding(team = teams[1], matchPoints = 9, gamePoints = 18, totalGames = 6, totalMatches = 3,
                        opponentMatchWinPct = .66, opponentGameWinPct = .5),
                SwissStanding(team = teams[2], matchPoints = 9, gamePoints = 18, totalGames = 7, totalMatches = 3,
                        opponentMatchWinPct = .66, opponentGameWinPct = .5),
                SwissStanding(team = teams[3], matchPoints = 9, gamePoints = 18, totalGames = 7, totalMatches = 3,
                        opponentMatchWinPct = .5, opponentGameWinPct = .66),
                SwissStanding(team = teams[4], matchPoints = 7, gamePoints = 16, totalGames = 7, totalMatches = 3,
                        opponentMatchWinPct = .66, opponentGameWinPct = .66)
        )

        assertStandings(expected = expected, actual = expected.shuffled().sortedWith(standingSorter))
    }

    fun createTeams(numberOfTeams: Int): List<SwissTeamItem> =
            (0 until numberOfTeams).map {
                SwissTeamItem(id = UUID.randomUUID().toString(), name = "team $it").apply {
                    players.setAll(
                            PlayerItem(id = UUID.randomUUID().toString()),
                            PlayerItem(id = UUID.randomUUID().toString()))
                }
            }


    fun pairing(winningTeam: SwissTeamItem,
                losingTeam: SwissTeamItem,
                winningTeamGameWins: Int = 1,
                losingTeamGameWins: Int = 0): SwissPairingItem =
            SwissPairingItem().apply {
                teamsProperty.setAll(winningTeam, losingTeam)
                gamesProperty.setAll(
                        (0 until winningTeamGameWins).map {
                            gameItem(winningTeam = winningTeam, losingTeam = losingTeam)
                        } + (0 until losingTeamGameWins).map {
                            gameItem(winningTeam = losingTeam, losingTeam = winningTeam)
                        }
                )
            }

    fun pairingTie(teams: Pair<SwissTeamItem, SwissTeamItem>): SwissPairingItem =
            SwissPairingItem(id = UUID.randomUUID().toString()).apply {
                teamsProperty.setAll(teams.toList())
                gamesProperty.setAll(SwissGameItem(
                        id = UUID.randomUUID().toString(),
                        winningTeamId = null,
                        gameId = UUID.randomUUID().toString()))
            }

    fun gameItem(winningTeam: SwissTeamItem, losingTeam: SwissTeamItem) =
            SwissGameItem(
                    id = UUID.randomUUID().toString(),
                    winningTeamId = winningTeam.id,
                    gameId = game(winningTeam = winningTeam.players, losingTeam = losingTeam.players).id)

    fun game(winningTeam: List<PlayerItem>, losingTeam: List<PlayerItem>): GameItem =
            GameItem(
                    id = UUID.randomUUID().toString(),
                    team1Players = winningTeam,
                    team2Players = losingTeam,
                    team1Score = 10,
                    team2Score = 1)
}

