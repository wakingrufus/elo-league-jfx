package com.github.wakingrufus.eloleague.swiss

data class SwissStanding(
        val team: SwissTeamItem,
        val matchPoints: Int = 0,
        val gamePoints: Int = 0,
        val totalMatches: Int = 0,
        val totalGames: Int = 0,
        val opponents: List<String> = ArrayList(),
        val opponentMatchWinPct: Double = 0.0,
        val opponentGameWinPct: Double = 0.0)
