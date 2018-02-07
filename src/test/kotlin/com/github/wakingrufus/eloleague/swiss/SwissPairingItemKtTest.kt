package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissGameData
import com.github.wakingrufus.eloleague.data.SwissPairingData
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class SwissPairingItemKtTest {
    val team1Id = UUID.randomUUID().toString()
    val team2Id = UUID.randomUUID().toString()

    @Test
    fun `test conversion`() {
        val data = SwissPairingData(
                id = UUID.randomUUID().toString(),
                teamIds = listOf(team1Id, team2Id),
                drops = listOf(team1Id),
                games = listOf(
                        SwissGameData(
                                id = UUID.randomUUID().toString(),
                                winningTeamId = team2Id,
                                gameId = UUID.randomUUID().toString()
                        )
                )
        )

        val tournament = SwissTournamentItem(
                id = UUID.randomUUID().toString()
        ).apply {
            teams.setAll(
                    SwissTeamItem(id = team1Id, name = "Team 1"),
                    SwissTeamItem(id = team2Id, name = "Team 2")
            )
        }

        val actual = fromPairingData(data = data, tournament = tournament)

        assertEquals(data, actual.toData())
    }

    @Test
    fun `test getWinningTeam 1`() {
        val instance = SwissPairingItem().apply {
            teamsProperty.setAll(SwissTeamItem(id = team1Id), SwissTeamItem(id = team2Id))
            gamesProperty.setAll(
                    SwissGameItem(gameId = UUID.randomUUID().toString(), winningTeamId = team1Id)
            )
        }
        assertEquals(team1Id, instance.getWinningTeam())
    }

    @Test
    fun `test getWinningTeam 2`() {
        val instance = SwissPairingItem().apply {
            teamsProperty.setAll(SwissTeamItem(id = team1Id), SwissTeamItem(id = team2Id))
            gamesProperty.setAll(
                    SwissGameItem(gameId = UUID.randomUUID().toString(), winningTeamId = team1Id),
                    SwissGameItem(gameId = UUID.randomUUID().toString(), winningTeamId = team2Id),
                    SwissGameItem(gameId = UUID.randomUUID().toString(), winningTeamId = team2Id)
            )
        }
        assertEquals(team2Id, instance.getWinningTeam())
    }

    @Test
    fun `test getWinningTeam bye`() {
        val instance = SwissPairingItem().apply {
            teamsProperty.setAll(SwissTeamItem(id = team1Id))
        }
        assertEquals(team1Id, instance.getWinningTeam())
    }


    @Test
    fun `test gamePoints`() {
        val teams = listOf(SwissTeamItem(), SwissTeamItem())
        assertEquals(6,
                SwissPairingItem().apply {
                    teamsProperty.setAll(teams)
                    gamesProperty.setAll(
                            SwissGameItem(winningTeamId = teams[0].id, gameId = UUID.randomUUID().toString()),
                            SwissGameItem(winningTeamId = teams[0].id, gameId = UUID.randomUUID().toString()),
                            SwissGameItem(winningTeamId = teams[1].id, gameId = UUID.randomUUID().toString())
                    )
                }.gamePoints(teams[0].id))

        assertEquals(3,
                SwissPairingItem().apply {
                    teamsProperty.setAll(teams)
                    gamesProperty.setAll(
                            SwissGameItem(winningTeamId = teams[0].id, gameId = UUID.randomUUID().toString()),
                            SwissGameItem(winningTeamId = teams[0].id, gameId = UUID.randomUUID().toString()),
                            SwissGameItem(winningTeamId = teams[1].id, gameId = UUID.randomUUID().toString())
                    )
                }.gamePoints(teams[1].id))

        assertEquals(6,
                SwissPairingItem().apply {
                    teamsProperty.setAll(teams[0])
                }.gamePoints(teams[0].id))
    }
}