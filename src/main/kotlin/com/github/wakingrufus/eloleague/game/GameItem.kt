package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.data.GameData
import com.github.wakingrufus.eloleague.player.PlayerItem
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.getValue
import tornadofx.setValue
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class GameItem(id: String,
               timestamp: String = DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now()),
               team1Players: List<PlayerItem> = FXCollections.observableArrayList(),
               team2Players: List<PlayerItem> = FXCollections.observableArrayList(),
               team1Score: Int = 0,
               team2Score: Int = 0) {
    val idProperty = SimpleStringProperty(this, "id", id)
    var id by idProperty

    val timestampProperty = SimpleStringProperty(this, "timestamp", timestamp)
    var timestamp by timestampProperty

    val team1PlayersProperty = SimpleListProperty<PlayerItem>(this, "team1Players", FXCollections.observableArrayList(team1Players))
    var team1Players by team1PlayersProperty

    val team2PlayersProperty = SimpleListProperty<PlayerItem>(this, "team2Players", FXCollections.observableArrayList(team2Players))
    var team2Players by team2PlayersProperty

    val team1ScoreProperty = SimpleIntegerProperty(team1Score, "team1Score", team1Score)
    var team1Score by team1ScoreProperty

    val team2ScoreProperty = SimpleIntegerProperty(team2Score, "team2Score", team2Score)
    var team2Score by team2ScoreProperty

}


fun toGameData(item: GameItem): GameData {
    return GameData(
            id = item.id,
            timestamp = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(item.timestamp)).toEpochMilli(),
            team1PlayerIds = item.team1Players.toList().map { it.id },
            team2PlayerIds = item.team2Players.toList().map { it.id },
            team1Score = item.team1Score,
            team2Score = item.team2Score
    )
}

fun fromData(data: GameData): GameItem {
    return GameItem(
            id = data.id,
            timestamp = DateTimeFormatter.ISO_DATE_TIME.format(Instant.ofEpochMilli(data.timestamp).atOffset(ZoneOffset.UTC)),
            team1Players = data.team1PlayerIds.toList().map { PlayerItem(id = it) },
            team2Players = data.team2PlayerIds.toList().map { PlayerItem(id = it) },
            team1Score = data.team1Score,
            team2Score = data.team2Score

    )
}
