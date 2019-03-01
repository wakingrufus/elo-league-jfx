package com.github.wakingrufus.eloleague.swiss

import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.ReadOnlyListWrapper
import mu.KLogging
import tornadofx.*

class StandingsView : Fragment() {
    companion object : KLogging()

    val standings: List<SwissStanding>  by param()

    override val root = borderpane {

        center {
            id = "standings-wrapper"

            tableview(ReadOnlyListWrapper(standings.observable())) {
                column<SwissStanding, String>("Name", { it.value.team.nameProperty })
                readonlyColumn("Points", SwissStanding::matchPoints)
                readonlyColumn("Opponent Match win %", SwissStanding::opponentMatchWinPct)
                column<SwissStanding, Number>("Game win %") {
                    ReadOnlyDoubleWrapper(gameWinPct(it.value))
                }
                readonlyColumn("Opponent Game win %", SwissStanding::opponentGameWinPct)
                columnResizePolicy = SmartResize.POLICY
            }

        }

    }
}
