package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissTeamData
import com.github.wakingrufus.eloleague.data.SwissTournamentData
import com.github.wakingrufus.eloleague.league.LeagueItem
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class SwissTournamentItem(id: String = UUID.randomUUID().toString(),
                          name: String = "",
                          startTime: String = DateTimeFormatter.ISO_DATE_TIME.format(Instant.now().atOffset(ZoneOffset.UTC))) {
    val idProperty = SimpleStringProperty(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val teamsProperty = SimpleListProperty<SwissTeamItem>(this, "teams", FXCollections.observableArrayList())
    var teams by teamsProperty

    val roundsProperty = SimpleListProperty<SwissRoundItem>(this, "rounds", FXCollections.observableArrayList())
    var rounds by roundsProperty

    val startTimeProperty = SimpleStringProperty(this, "startTime", startTime)
    var startTime by startTimeProperty

    fun toData(): SwissTournamentData =
            SwissTournamentData(
                    id = this.id,
                    name = this.name,
                    teams = this.teams.map {
                        SwissTeamData(
                                id = it.id,
                                name = it.name,
                                playerIds = it.players.map { it.id }.toSet())
                    },
                    rounds = rounds.map(SwissRoundItem::toData),
                    startTime = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(this.startTime)).toEpochMilli())
}

fun fromTournamentData(data: SwissTournamentData, league: LeagueItem): SwissTournamentItem {
    return SwissTournamentItem(
            id = data.id,
            name = data.name,
            startTime = DateTimeFormatter.ISO_DATE_TIME.format(Instant.ofEpochMilli(data.startTime).atOffset(ZoneOffset.UTC))
    ).apply {
        teams.setAll(data.teams.map { teamData ->
            SwissTeamItem(id = teamData.id, name = teamData.name).apply {
                players.addAll(teamData.playerIds.map { teamPlayerId ->
                    league.playersProperty.first { playerItem ->
                        playerItem.id == teamPlayerId
                    }
                })
            }
        })
        rounds.setAll(data.rounds.map { fromRoundData(data = it, tournament = this) })
    }
}
