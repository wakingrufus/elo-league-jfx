package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissTeamData
import com.github.wakingrufus.eloleague.data.SwissTournamentData
import com.github.wakingrufus.eloleague.league.LeagueItem
import com.github.wakingrufus.eloleague.player.PlayerItem
import org.junit.Test
import java.time.Instant
import java.util.*
import kotlin.test.assertEquals

class SwissTournamentItemKtTest {
    @Test
    fun `test fromData`() {
        val player1Id = UUID.randomUUID().toString()
        val league = LeagueItem(id = UUID.randomUUID().toString()).apply {
            players.setAll(listOf(PlayerItem(id = player1Id, name = "playerName")))
        }

        val actual = fromTournamentData(
                data = SwissTournamentData(
                        id = UUID.randomUUID().toString(),
                        name = "name",
                        startTime = Instant.now().toEpochMilli(),
                        teams = listOf(
                                SwissTeamData(id = UUID.randomUUID().toString(), name = "teamName", playerIds = setOf(player1Id))
                        ),
                        rounds = listOf()),
                league = league
        )
        assertEquals(1, actual.teams.size)
        val actualTeam = actual.teams[0]
        assertEquals("teamName", actualTeam.name)
        assertEquals(1, actual = actualTeam.players.size)
        val actualPlayerItem = actualTeam.players[0]
        assertEquals("playerName", actualPlayerItem.name)
    }
}