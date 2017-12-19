package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.elo.Game
import com.github.wakingrufus.elo.League
import com.github.wakingrufus.eloleague.data.GameData
import com.github.wakingrufus.eloleague.data.LeagueData
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