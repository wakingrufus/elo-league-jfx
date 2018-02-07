package com.github.wakingrufus.eloleague.swiss

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.test.assertNull

class SwissPairingItemTest {
    @Test
    fun `test getWinningTeam`() {
        val team1Id = UUID.randomUUID().toString()
        val team2Id = UUID.randomUUID().toString()

        assertEquals(team1Id, SwissPairingItem(
                id = UUID.randomUUID().toString()
        ).apply {
            teamsProperty.setAll(teams(team1Id, team2Id))
            gamesProperty.setAll(game(team1Id), game(team1Id), game(team2Id))
        }.getWinningTeam())

        assertEquals(team2Id, SwissPairingItem(
                id = UUID.randomUUID().toString()
        ).apply {
            teamsProperty.setAll(teams(team1Id, team2Id))
            gamesProperty.setAll(game(team1Id), game(team2Id), game(team2Id))
        }.getWinningTeam())

        assertNull(SwissPairingItem(
                id = UUID.randomUUID().toString()
        ).apply {
            teamsProperty.setAll(teams(team1Id, team2Id))
            gamesProperty.setAll(game(team1Id), game(team2Id))
        }.getWinningTeam())
    }


    fun teams(vararg ids: String): List<SwissTeamItem> =
            ids.map(this::team)

    fun team(id: String): SwissTeamItem = SwissTeamItem(id = id)

    fun game(winningTeamId: String): SwissGameItem = SwissGameItem(
            id = UUID.randomUUID().toString(),
            winningTeamId = winningTeamId,
            gameId = UUID.randomUUID().toString())
}