package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissRound
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections

class SwissRoundItem(roundNumber: Int) {
    val roundNumberProperty = SimpleIntegerProperty(this, "roundNumber", roundNumber)
    val pairingsProperty = SimpleListProperty<SwissPairingItem>(this, "pairings", FXCollections.observableArrayList())

    fun toData(): SwissRound = SwissRound(
            roundNumber = roundNumberProperty.value,
            pairings = pairingsProperty.value.map(SwissPairingItem::toData))
}


fun fromRoundData(data: SwissRound, tournament: SwissTournamentItem): SwissRoundItem =
        SwissRoundItem(data.roundNumber).apply {
            this.pairingsProperty.setAll(data.pairings
                    .map { fromPairingData(data = it, tournament = tournament) })
        }
