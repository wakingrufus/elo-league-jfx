package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.elo.League
import com.github.wakingrufus.eloleague.data.LeagueData
import com.github.wakingrufus.eloleague.game.GameItem
import com.github.wakingrufus.eloleague.game.toData
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.swiss.SwissTournamentItem
import com.github.wakingrufus.eloleague.swiss.toData
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*

val defaultLeague: League = League()

class LeagueItem(id: String,
                 name: String = "",
                 startingRating: Int = defaultLeague.startingRating,
                 xi: Int = defaultLeague.xi,
                 kFactorBase: Int = defaultLeague.kFactorBase,
                 trialPeriod: Int = defaultLeague.trialPeriod,
                 trialKFactorMultiplier: Int = defaultLeague.trialKFactorMultiplier) {
    val idProperty = SimpleStringProperty(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val startingRatingProperty = SimpleIntegerProperty(this, "startingRating", startingRating)
    var startingRating by startingRatingProperty

    val xiProperty = SimpleIntegerProperty(this, "xi", xi)
    var xi by xiProperty

    val kFactorBaseProperty = SimpleIntegerProperty(this, "kFactorBase", kFactorBase)
    var kFactorBase by kFactorBaseProperty

    val trialPeriodProperty = SimpleIntegerProperty(this, "trialPeriod", trialPeriod)
    var trialPeriod by trialPeriodProperty

    val trialKFactorMultiplierProperty = SimpleIntegerProperty(this, "trialKFactorMultiplier", trialKFactorMultiplier)
    var trialKFactorMultiplier by trialKFactorMultiplierProperty

    val playersProperty = SimpleListProperty<PlayerItem>(this, "players", FXCollections.observableArrayList())
    var players by playersProperty

    val gamesProperty = SimpleListProperty<GameItem>(this, "games", FXCollections.observableArrayList())
    var games by gamesProperty

    val tournamentsProperty = SimpleListProperty<SwissTournamentItem>(this, "tournaments", FXCollections.observableArrayList())
    var tournaments by tournamentsProperty

}

fun toData(leagueItem: LeagueItem): LeagueData {
    return LeagueData(
            id = leagueItem.id,
            name = leagueItem.name,
            startingRating = leagueItem.startingRating,
            xi = leagueItem.xi,
            kFactorBase = leagueItem.kFactorBase,
            trialPeriod = leagueItem.trialPeriod,
            trialKFactorMultiplier = leagueItem.trialKFactorMultiplier,
            players = leagueItem.players.map { com.github.wakingrufus.eloleague.player.toData(it) }.toSet(),
            games = leagueItem.games.map(GameItem::toData).toList(),
            tournamentData = leagueItem.tournaments.map(SwissTournamentItem::toData).toList()
    )
}

fun fromData(leagueData: LeagueData): LeagueItem {
    val item = LeagueItem(
            id = leagueData.id,
            name = leagueData.name,
            startingRating = leagueData.startingRating,
            xi = leagueData.xi,
            kFactorBase = leagueData.kFactorBase,
            trialPeriod = leagueData.trialPeriod,
            trialKFactorMultiplier = leagueData.trialKFactorMultiplier
    )
    item.playersProperty.setAll(leagueData.players.map { com.github.wakingrufus.eloleague.player.fromData(it) })
    item.gamesProperty.setAll(leagueData.games.map { com.github.wakingrufus.eloleague.game.fromData(it) })
    item.games.forEach({ game ->
        game.team1Players.forEach({ player ->
            player.name = item.players.first { it.id == player.id }?.name
        })
        game.team2Players.forEach({ player ->
            player.name = item.players.first { it.id == player.id }?.name
        })
    })
    if (leagueData.tournamentData.isNotEmpty()) {
        item.tournamentsProperty.setAll(leagueData.tournamentData.map { com.github.wakingrufus.eloleague.swiss.fromData(it, item.players) })
    }
    return item
}
