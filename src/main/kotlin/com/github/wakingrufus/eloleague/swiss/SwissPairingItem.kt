package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissPairingData
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import java.util.*

class SwissPairingItem(val id: String = UUID.randomUUID().toString()) {
    val teamsProperty = SimpleListProperty<SwissTeamItem>(this, "teams", FXCollections.observableArrayList())
    val gamesProperty = SimpleListProperty<SwissGameItem>(this, "games", FXCollections.observableArrayList())
    val dropsProperty = SimpleListProperty<String>(this, "drops", FXCollections.observableArrayList())

    fun getWinningTeam(): String? = when {
        teamsProperty.size == 1 -> teamsProperty[0].id // bye round
        gamePoints(teamsProperty[0].id) > gamePoints(teamsProperty[1].id) -> teamsProperty[0].id
        gamePoints(teamsProperty[1].id) > gamePoints(teamsProperty[0].id) -> teamsProperty[1].id
        else -> null
    }

    fun toData(): SwissPairingData = SwissPairingData(
            id = id,
            teamIds = teamsProperty.map { it.id },
            games = gamesProperty.map { it.toData() },
            drops = dropsProperty.value)


    fun gamePoints(teamId: String): Int =
            when {
                this.teamsProperty.size == 1 && teamsProperty[0].id == teamId -> 6  // bye round
                this.teamsProperty.size == 1 && teamsProperty[0].id != teamId -> 0  // bye round
                else -> this.gamesProperty.map {
                    when {
                        it.winningTeamIdProperty.isNull.value -> 1
                        it.winningTeamIdProperty.value == teamId -> 3
                        else -> 0
                    }
                }.sum()
            }
}

fun fromPairingData(data: SwissPairingData, tournament: SwissTournamentItem): SwissPairingItem =
        SwissPairingItem(id = data.id).apply {
            teamsProperty.setAll(tournament.teams.filter { data.teamIds.contains(it.id) })
            gamesProperty.setAll(data.games.map(::fromSwissGameData))
            dropsProperty.setAll(data.drops)
        }
