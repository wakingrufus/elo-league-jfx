package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.elo.Game
import com.github.wakingrufus.elo.League
import com.github.wakingrufus.elo.LeagueState
import com.github.wakingrufus.elo.Player
import com.github.wakingrufus.eloleague.data.GameData
import com.github.wakingrufus.eloleague.data.LeagueData
import com.github.wakingrufus.eloleague.player.PlayerItem
import java.time.Instant

fun league(leagueData: LeagueData): League {
    return League(startingRating = leagueData.startingRating,
            kFactorBase = leagueData.kFactorBase,
            trialKFactorMultiplier = leagueData.trialKFactorMultiplier,
            xi = leagueData.xi,
            trialPeriod = leagueData.trialPeriod,
            teamSize = leagueData.teamSize)
}

fun game(gameData: GameData): Game {
    return Game(
            id = gameData.id,
            entryDate = Instant.ofEpochMilli(gameData.timestamp),
            team1PlayerIds = gameData.team1PlayerIds,
            team2PlayerIds = gameData.team2PlayerIds,
            team1Score = gameData.team1Score,
            team2Score = gameData.team2Score
    )
}

fun games(games: List<GameData>): List<Game> {
    return games.map { game(it) }
}

fun player(player: Player,
           playerItem: PlayerItem): PlayerResultItem {
    return PlayerResultItem(
            id = player.id,
            name = playerItem.name,
            currentRating = player.currentRating,
            gamesPlayed = player.gamesPlayed,
            wins = player.wins,
            losses = player.losses)
}

fun results(players: List<PlayerItem>,
            leagueState: LeagueState): LeagueResultItem {
    val resultItem = LeagueResultItem()
    resultItem.players.setAll(leagueState.players.values
            .map { player -> player(player, players.first { it.id == player.id }) })
    /*
     resultItem.games.setAll(leagueState.history
             .map { game -> GameResultItem(
                     ratingHistoryRecord = game,
                     gameRecord = games.first{it.id == game.gameId}) })
                     */
    return resultItem
}