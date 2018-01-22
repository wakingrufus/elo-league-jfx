package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissTeamData
import com.github.wakingrufus.eloleague.data.SwissTournamentData
import com.github.wakingrufus.eloleague.player.PlayerItem
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleMapProperty
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

    val roundsProperty = SimpleMapProperty<Int, SwissRoundItem>(this, "rounds", FXCollections.observableHashMap())
    var rounds by roundsProperty

    val startTimeProperty = SimpleStringProperty(this, "startTime", startTime)
    var startTime by startTimeProperty
}


fun SwissTournamentItem.toData(): SwissTournamentData =
        SwissTournamentData(
                id = this.id,
                name = this.name,
                teams = this.teams.map {
                    SwissTeamData(
                            id = it.id,
                            name = it.name,
                            playerIds = it.players.map { it.id }.toSet())
                },
                rounds = mapOf(),
                startTime = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(this.startTime)).toEpochMilli()
        )


fun fromData(data: SwissTournamentData, leaguePlayers: List<PlayerItem>)
        : SwissTournamentItem {
    val tournamentItem = SwissTournamentItem(
            id = data.id,
            name = data.name,
            startTime = DateTimeFormatter.ISO_DATE_TIME.format(Instant.ofEpochMilli(data.startTime).atOffset(ZoneOffset.UTC))
    )
    tournamentItem.teams.setAll(data.teams.map { teamData ->
        val teamItem =
                SwissTeamItem(id = teamData.id,
                        name = teamData.name)
        teamItem.players.addAll(teamData.playerIds
                .map { teamPlayerId ->
                    leaguePlayers
                            .first { playerItem ->
                                playerItem.id == teamPlayerId
                            }
                })
        teamItem
    })
    data.rounds.keys.sorted().map {
        data.rounds[it]
    }.map {

    }
    //  tournamentItem.rounds.se
    return tournamentItem
}
