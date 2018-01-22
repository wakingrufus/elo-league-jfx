package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissPairing
import com.github.wakingrufus.eloleague.data.SwissResultData
import javafx.beans.property.SimpleMapProperty
import javafx.collections.ObservableMap

class SwissRoundItem(val pairings: ObservableMap<SwissPairing, SwissResultData?>) {
    val pairingsProperty = SimpleMapProperty<SwissPairing, SwissResultData?>(this, "pairings", pairings)


    fun isComplete() {
        pairingsProperty.all { it.value != null }
    }

}