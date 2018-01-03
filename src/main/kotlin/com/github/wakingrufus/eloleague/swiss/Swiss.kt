package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissPairing
import com.github.wakingrufus.eloleague.data.SwissTeamData
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt


fun getPairings(tournamet: SwissTournamentItem) {

}

fun reportResult() {

}

fun totalRounds(teams: Int): Int {
    return teams.toDouble().pow(2).roundToInt()
}


fun isSamePairing(pairing1: SwissPairing, pairing2: SwissPairing): Boolean {
    return pairing1.teamIds == pairing2.teamIds
            || pairing1.teamIds == Pair(pairing2.teamIds.second, pairing2.teamIds.first)
}


fun randomPairing(vararg teams: SwissTeamData): List<SwissPairing> {
    return teams.take(teams.size / 2).toList().shuffled()
            .zip(teams.takeLast(teams.size / 2).shuffled())
            .map {
                SwissPairing(id = UUID.randomUUID().toString(), teamIds = Pair(it.first.id, it.second.id))
            }
            .toList()
}

fun pairing(records: List<SwissStanding>, previousPairings: List<SwissPairing>) {
    val playersToPair = mutableListOf(records.sortedWith(standingSorter))
    val pairings: List<SwissPairing> = mutableListOf()
    records.forEach { currentPlayer ->
        //  if (playersToPair.contains(currentPlayer)) {
        //  val opponent = playersToPair.firstOrNull { potentialOpponent ->
        //      previousPairings.none { isSamePairing(it.pair, Pair(currentPlayer, potentialOpponent)) }
        //  }
        //  }
    }

}
