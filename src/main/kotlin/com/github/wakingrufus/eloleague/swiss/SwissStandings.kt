package com.github.wakingrufus.eloleague.swiss

import java.util.*

data class SwissStanding(
        val team: SwissTeamItem,
        val points: Int = 0,
        val gameWins: Int = 0,
        val winPct: Double = 0.0,
        val opponentWinPct: Double = 0.0
)

val standingSorter: Comparator<SwissStanding> =
        Comparator.comparingInt(SwissStanding::points)
                .then(Comparator.comparing(SwissStanding::gameWins))

fun sortStandings(records: List<SwissStanding>) {
    records.sortedWith(standingSorter)
}
